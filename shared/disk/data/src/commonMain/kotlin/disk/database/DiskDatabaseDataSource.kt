package disk.database

import androidx.room.Transactor.SQLiteTransactionType
import androidx.room.useWriterConnection
import core.storage.impl.room.AppDatabase
import core.storage.impl.room.disk.DiskResourceDao
import core.storage.impl.room.disk.DiskSyncOperationDao
import disk.models.resources.DiskResource
import disk.models.sync.SyncOperation
import disk.models.sync.SyncOperationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import utils.types.DiskPath

class DiskDatabaseDataSource(
    private val database: AppDatabase,
    private val resourceDao: DiskResourceDao,
    private val syncOperationDao: DiskSyncOperationDao,
) {
    fun observeDirectory(path: DiskPath): Flow<List<DiskResource>> {
        return resourceDao.observeParentPathContent(path.value).map { resources ->
            resources.map { it.toDomain() }
        }
    }

    suspend fun getDirectory(path: DiskPath): List<DiskResource> {
        return resourceDao.getParentPathContent(path.value).map { it.toDomain() }
    }

    suspend fun getResource(path: DiskPath): DiskResource? {
        return resourceDao.getByPath(path.value)?.toDomain()
    }

    suspend fun getResourceByLocalId(localId: String): DiskResource? {
        return resourceDao.getByLocalId(localId)?.toDomain()
    }

    suspend fun applyRemoteResource(resource: DiskResource) {
        resourceDao.upsert(resource.toEntity())
    }

    suspend fun deleteRemoteMissingResource(localId: String) {
        resourceDao.deleteByLocalId(localId)
    }

    suspend fun upsertResourceWithSyncOperation(
        resource: DiskResource,
        operation: SyncOperation,
    ) = transaction {
        resourceDao.upsert(resource.toEntity())
        syncOperationDao.upsert(operation.toEntity())
    }

    suspend fun deleteResourceWithSyncOperation(
        localId: String,
        operation: SyncOperation,
    ) = transaction {
        resourceDao.deleteByLocalId(localId)
        syncOperationDao.upsert(operation.toEntity())
    }

    suspend fun replaceResourceWithSyncOperation(
        oldLocalId: String,
        resource: DiskResource,
        operation: SyncOperation,
    ) = transaction {
        resourceDao.deleteByLocalId(oldLocalId)
        resourceDao.upsert(resource.toEntity())
        syncOperationDao.upsert(operation.toEntity())
    }

    suspend fun applySyncedResourceAndDeleteOperation(
        resource: DiskResource,
        operationId: String,
    ) = transaction {
        resourceDao.upsert(resource.toEntity())
        syncOperationDao.deleteById(operationId)
    }

    // ====================== SYNC ======================

    fun observeSyncOperations(): Flow<List<SyncOperation>> {
        return syncOperationDao.observeAll().map { operations ->
            operations.map { it.toDomain() }
        }
    }

    suspend fun getSyncOperationsByStates(
        states: List<SyncOperationState>,
    ): List<SyncOperation> {
        return syncOperationDao.getByStates(states.map { it.name }).map { it.toDomain() }
    }

    suspend fun updateSyncOperationState(
        id: String,
        state: SyncOperationState,
    ) {
        syncOperationDao.updateState(
            id = id,
            state = state.name,
        )
    }

    suspend fun markSyncOperationFailed(
        id: String,
        lastError: String?,
    ) {
        syncOperationDao.updateFailure(
            id = id,
            state = SyncOperationState.FAILED.name,
            lastError = lastError,
        )
    }

    suspend fun deleteSyncOperation(id: String) {
        syncOperationDao.deleteById(id)
    }

    private suspend fun <T> transaction(block: suspend () -> T): T {
        return database.useWriterConnection { transactor ->
            transactor.withTransaction(SQLiteTransactionType.IMMEDIATE) {
                block()
            }
        }
    }
}
