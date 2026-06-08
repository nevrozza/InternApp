package disk.database

import core.storage.impl.room.disk.DiskResourceDao
import core.storage.impl.room.disk.DiskSyncOperationDao
import disk.models.resources.DiskResource
import disk.models.sync.SyncOperation
import disk.models.sync.SyncOperationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import utils.types.DiskPath

class DiskDatabaseDataSource(
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

    suspend fun upsertResource(resource: DiskResource) {
        resourceDao.upsert(resource.toEntity())
    }

    suspend fun upsertResources(resources: List<DiskResource>) {
        resourceDao.upsert(resources.map { it.toEntity() })
    }

    suspend fun deleteResource(path: DiskPath) {
        resourceDao.deleteByPath(path.value)
    }

    suspend fun deleteResourceByLocalId(localId: String) {
        resourceDao.deleteByLocalId(localId)
    }


    // ====================== SYNC ======================

    fun observeSyncOperations(): Flow<List<SyncOperation>> {
        return syncOperationDao.observeAll().map { operations ->
            operations.map { it.toDomain() }
        }
    }

    suspend fun getSyncOperationsByState(
        state: SyncOperationState,
    ): List<SyncOperation> {
        return syncOperationDao.getByState(state.name).map { it.toDomain() }
    }

    suspend fun getSyncOperationsByStates(
        states: List<SyncOperationState>,
    ): List<SyncOperation> {
        return syncOperationDao.getByStates(states.map { it.name }).map { it.toDomain() }
    }

    suspend fun getActiveSyncOperationByResourceLocalId(
        resourceLocalId: String,
        states: List<SyncOperationState>,
    ): SyncOperation? {
        return syncOperationDao
            .getActiveByResourceLocalId(
                resourceLocalId = resourceLocalId,
                states = states.map { it.name },
            )
            ?.toDomain()
    }

    suspend fun upsertSyncOperation(operation: SyncOperation) {
        syncOperationDao.upsert(operation.toEntity())
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
}
