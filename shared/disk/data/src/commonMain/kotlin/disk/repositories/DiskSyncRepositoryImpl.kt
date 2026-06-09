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
import disk.network.dto.DiskResourceResponse
import disk.network.toDomain
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import utils.types.isTextFileName

class DiskSyncRepositoryImpl(
    private val database: DiskDatabaseDataSource,
    private val remote: DiskRemoteDataSource,
) : DiskSyncRepository {

    private val syncMutex = Mutex()

    override fun observeOperations(): Flow<List<SyncOperation>> {
        return database.observeSyncOperations()
    }

    override suspend fun sync() = syncMutex.withLock {
        val operations = database.getSyncOperationsByStates(
            listOf(SyncOperationState.PENDING, SyncOperationState.FAILED),
        )
        operations.forEach { operation ->
            syncOperation(operation)
        }
    }

    override suspend fun cancelLocal(operation: SyncOperation) {
        database.cancelLocalSyncOperation(operation)
    }

    override suspend fun clearLocalState() = syncMutex.withLock {
        database.clear()
    }

    private suspend fun syncOperation(operation: SyncOperation) {
        database.updateSyncOperationState(operation.id, SyncOperationState.RUNNING)

        runCatching {
            when (operation.operation) {
                SyncOperationType.CREATE_FOLDER -> syncCreateFolder(operation)
                SyncOperationType.DELETE -> syncDelete(operation)
                SyncOperationType.RENAME -> syncRename(operation)
                SyncOperationType.UPSERT_TEXT_FILE -> syncUpsertTextFile(operation)
                SyncOperationType.UPLOAD_FILE -> syncUploadFile(operation)
            }
        }.onFailure { error ->
            database.markSyncOperationFailed(
                id = operation.id,
                lastError = error.message ?: "Unknown sync error",
            )
        }
    }

    private suspend fun syncCreateFolder(operation: SyncOperation) {
        try {
            remote.createFolder(operation.path.value)
        } catch (error: ClientRequestException) {
            if (!error.isConflict() || !completeIfRemoteResourceExists(operation) { response ->
                    response.type == REMOTE_TYPE_DIRECTORY
                }
            ) {
                throw error
            }
            return
        }
        refreshSyncedResource(operation)
    }

    private suspend fun syncDelete(operation: SyncOperation) {
        try {
            remote.delete(operation.path.value)
        } catch (error: ClientRequestException) {
            if (error.response.status != HttpStatusCode.NotFound) throw error
        }
        database.deleteSyncOperation(operation.id)
    }

    private suspend fun syncRename(operation: SyncOperation) {
        val targetPath = checkNotNull(operation.targetPath)
        val targetOperation = operation.copy(path = targetPath)

        try {
            remote.rename(
                sourcePath = operation.path.value,
                targetPath = targetPath.value,
            )
        } catch (error: ClientRequestException) {
            if (
                (!error.isConflict() && !error.isNotFound()) ||
                !completeIfRemoteResourceIsEquivalent(targetOperation)
            ) {
                throw error
            }
            return
        }
        refreshSyncedResource(targetOperation)
    }

    private suspend fun syncUpsertTextFile(operation: SyncOperation) {
        val resource = database.getResourceByLocalId(operation.resourceLocalId ?: return)
        val textFile = resource as? TextFileResource ?: return
        val textOperation = operation.copy(path = textFile.path)

        try {
            remote.uploadText(
                path = textFile.path.value,
                content = textFile.textContent,
            )
        } catch (error: ClientRequestException) {
            if (!error.isConflict() || !completeIfRemoteResourceIsEquivalent(textOperation)) {
                throw error
            }
            return
        }
        refreshSyncedResource(textOperation)
    }

    private suspend fun syncUploadFile(operation: SyncOperation) {
        val resource = database.getResourceByLocalId(operation.resourceLocalId ?: return)
        val file = resource as? BinaryFileResource ?: return
        checkNotNull(file.localFilePath) { "Local file path is missing" }
        error("Binary file upload not yet supported =/")
    }

    private suspend fun refreshSyncedResource(operation: SyncOperation) {
        val response = remote.getResource(operation.path.value)
        val current = operation.resourceLocalId?.let { database.getResourceByLocalId(it) }
        val textContent = (current as? TextFileResource)?.textContent
        val refreshed = response.toDomain(
            localId = operation.resourceLocalId,
            textContent = textContent,
        )

        database.applySyncedResourceAndDeleteOperation(
            resource = refreshed,
            operationId = operation.id,
        )
    }

    private suspend fun completeIfRemoteResourceExists(
        operation: SyncOperation,
        predicate: suspend (DiskResourceResponse) -> Boolean,
    ): Boolean {
        val response = try {
            remote.getResource(operation.path.value)
        } catch (error: ClientRequestException) {
            if (error.isNotFound()) return false
            throw error
        }

        if (!predicate(response)) return false

        refreshSyncedResource(operation)
        return true
    }

    private suspend fun completeIfRemoteResourceIsEquivalent(operation: SyncOperation): Boolean {
        val local = operation.resourceLocalId
            ?.let { database.getResourceByLocalId(it) }
            ?: return false

        return completeIfRemoteResourceExists(operation) { response ->
            local.isEquivalentTo(response)
        }
    }

    private suspend fun DiskResource.isEquivalentTo(
        response: DiskResourceResponse,
    ): Boolean {
        return when (this) {
            is TextFileResource -> {
                response.type != REMOTE_TYPE_DIRECTORY &&
                    response.name.isTextFileName() &&
                    textContent == remote.downloadText(response.path)
            }

            is BinaryFileResource -> {
                md5 != null && md5 == response.md5
            }

            is DirectoryResource -> {
                response.type == REMOTE_TYPE_DIRECTORY &&
                    (resourceId == null || resourceId == response.resourceId)
            }
        }
    }

    private fun ClientRequestException.isConflict(): Boolean {
        return response.status == HttpStatusCode.Conflict
    }

    private fun ClientRequestException.isNotFound(): Boolean {
        return response.status == HttpStatusCode.NotFound
    }

    private companion object {
        const val REMOTE_TYPE_DIRECTORY = "dir"
    }
}
