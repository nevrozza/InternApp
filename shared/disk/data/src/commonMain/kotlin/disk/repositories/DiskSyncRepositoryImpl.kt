package disk.repositories

import disk.database.DiskDatabaseDataSource
import disk.models.resources.BinaryFileResource
import disk.models.resources.TextFileResource
import disk.models.sync.SyncOperation
import disk.models.sync.SyncOperationState
import disk.models.sync.SyncOperationType
import disk.network.DiskRemoteDataSource
import disk.network.toDomain
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow

class DiskSyncRepositoryImpl(
    private val database: DiskDatabaseDataSource,
    private val remote: DiskRemoteDataSource,
) : DiskSyncRepository {

    override fun observeOperations(): Flow<List<SyncOperation>> {
        return database.observeSyncOperations()
    }

    override suspend fun sync() {
        val operations = database.getSyncOperationsByStates(
            listOf(SyncOperationState.PENDING, SyncOperationState.FAILED),
        )
        operations.forEach { operation ->
            syncOperation(operation)
        }
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
        remote.createFolder(operation.path.value)
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
        remote.rename(
            sourcePath = operation.path.value,
            targetPath = targetPath.value,
        )
        refreshSyncedResource(operation.copy(path = targetPath))
    }

    private suspend fun syncUpsertTextFile(operation: SyncOperation) {
        val resource = database.getResourceByLocalId(operation.resourceLocalId ?: return)
        val textFile = resource as? TextFileResource ?: return
        remote.uploadText(
            path = textFile.path.value,
            content = textFile.textContent,
        )
        refreshSyncedResource(operation.copy(path = textFile.path))
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
}
