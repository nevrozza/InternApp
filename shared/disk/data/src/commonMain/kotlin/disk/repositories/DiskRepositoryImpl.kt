package disk.repositories

import disk.database.DiskDatabaseDataSource
import disk.models.resources.BinaryFileResource
import disk.models.resources.DirectoryResource
import disk.models.resources.DiskResource
import disk.models.resources.TextFileResource
import disk.models.sync.SyncOperation
import disk.models.sync.SyncOperationState
import disk.models.sync.SyncOperationType
import disk.network.DiskRemoteDataSource
import disk.network.toDomain
import kotlinx.coroutines.flow.Flow
import utils.types.DiskPath
import utils.types.LocalPath
import utils.types.isTextFileName
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DiskRepositoryImpl(
    private val database: DiskDatabaseDataSource,
    private val remote: DiskRemoteDataSource,
) : DiskRepository {


    @OptIn(ExperimentalUuidApi::class)
    private fun generateUUIDv7String(): String {
        return Uuid.generateV7().toString()
    }

    override fun observeDirectory(path: DiskPath): Flow<List<DiskResource>> {
        return database.observeDirectory(path)
    }

    override suspend fun refreshDirectory(path: DiskPath) {
        val activeOperations = database.getSyncOperationsByStates(ActiveSyncStates)
        val remoteResources = remote.listDirectory(path.value)
        val localResources = database.getDirectory(path)

        val remotePaths = remoteResources.mapTo(mutableSetOf()) { DiskPath(it.path) }
        val localByPath = localResources.associateBy { it.path }

        remoteResources.forEach { response ->
            val remotePath = DiskPath(response.path)
            val local = localByPath[remotePath]

            if (isLockedBySync(remotePath, local, activeOperations)) return@forEach

            val serverResource = response.toDomain(localId = local?.localId)
            if (local == null || serverResource.modified > local.modified) {
                val resourceToSave = if (response.name.isTextFileName()) {
                    response.toDomain(
                        localId = local?.localId,
                        textContent = remote.downloadText(response.path),
                    )
                } else {
                    serverResource
                }
                database.applyRemoteResource(resourceToSave)
            }
        }

        localResources.forEach { local ->
            if (local.path !in remotePaths && !isLockedBySync(
                    local.path,
                    local,
                    activeOperations
                )
            ) {
                database.deleteRemoteMissingResource(local.localId)
            }
        }
    }

    override suspend fun getResource(path: DiskPath): DiskResource? {
        return database.getResource(path)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createFolder(parentPath: DiskPath, name: String) {
        val path = parentPath.child(name)
        val resource = DirectoryResource(
            localId = generateUUIDv7String(),
            resourceId = null,
            path = path,
            parentPath = parentPath,
            name = name,
            modified = now(),
        )

        database.upsertResourceWithSyncOperation(
            resource = resource,
            operation = resource.syncOperation(SyncOperationType.CREATE_FOLDER),
        )
    }

    override suspend fun delete(path: DiskPath) {
        val resource = database.getResource(path) ?: return

        database.deleteResourceWithSyncOperation(
            localId = resource.localId,
            operation = resource.syncOperation(SyncOperationType.DELETE),
        )
    }

    override suspend fun rename(sourcePath: DiskPath, newName: String) {
        val resource = database.getResource(sourcePath) ?: return
        val targetPath = sourcePath.withName(newName)
        val renamed = resource.withPath(
            path = targetPath,
            parentPath = targetPath.parent(),
            name = newName,
            modified = now(),
        )

        database.replaceResourceWithSyncOperation(
            oldLocalId = resource.localId,
            resource = renamed,
            operation = renamed.syncOperation(
                operation = SyncOperationType.RENAME,
                path = sourcePath,
                targetPath = targetPath,
            ),
        )
    }

    override suspend fun saveTextFile(path: DiskPath, content: String) {
        val resource = when (val existing = database.getResource(path)) {
            is TextFileResource -> existing.copy(
                textContent = content,
                modified = now(),
            )

            null -> TextFileResource(
                localId = generateUUIDv7String(),
                resourceId = null,
                path = path,
                parentPath = path.parent(),
                name = path.name(),
                modified = now(),
                md5 = null,
                textContent = content,
            )

            else -> return
        }

        database.upsertResourceWithSyncOperation(
            resource = resource,
            operation = resource.syncOperation(SyncOperationType.UPSERT_TEXT_FILE),
        )
    }

    override suspend fun uploadFile(
        parentPath: DiskPath,
        name: String,
        localFilePath: LocalPath,
    ) {
        val path = parentPath.child(name)
        val resource = BinaryFileResource(
            localId = generateUUIDv7String(),
            resourceId = null,
            path = path,
            parentPath = parentPath,
            name = name,
            modified = now(),
            md5 = null,
            localFilePath = localFilePath,
        )

        database.upsertResourceWithSyncOperation(
            resource = resource,
            operation = resource.syncOperation(SyncOperationType.UPLOAD_FILE),
        )
    }

    private fun isLockedBySync(
        path: DiskPath,
        resource: DiskResource?,
        operations: List<SyncOperation>,
    ): Boolean {
        return operations.any { operation ->
            operation.resourceLocalId == resource?.localId ||
                    operation.resourceId == resource?.resourceId ||
                    operation.path == path ||
                    operation.targetPath == path
        }
    }

    private fun DiskResource.syncOperation(
        operation: SyncOperationType,
        path: DiskPath = this.path,
        targetPath: DiskPath? = null,
    ): SyncOperation {
        return SyncOperation(
            id = generateUUIDv7String(),
            resourceLocalId = localId,
            resourceId = resourceId,
            operation = operation,
            path = path,
            targetPath = targetPath,
            state = SyncOperationState.PENDING,
            lastError = null,
        )
    }

    private fun DiskResource.withPath(
        path: DiskPath,
        parentPath: DiskPath,
        name: String,
        modified: Instant,
    ): DiskResource {
        return when (this) {
            is DirectoryResource -> copy(
                path = path,
                parentPath = parentPath,
                name = name,
                modified = modified
            )

            is TextFileResource -> copy(
                path = path,
                parentPath = parentPath,
                name = name,
                modified = modified
            )

            is BinaryFileResource -> copy(
                path = path,
                parentPath = parentPath,
                name = name,
                modified = modified
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun now(): Instant { return Clock.System.now() }

    private companion object {
        val ActiveSyncStates = listOf(
            SyncOperationState.PENDING,
            SyncOperationState.RUNNING,
            SyncOperationState.FAILED,
        )
    }
}
