package disk.repositories

import disk.models.sync.SyncOperation
import kotlinx.coroutines.flow.Flow

interface DiskSyncRepository {
    fun observeOperations(): Flow<List<SyncOperation>>
    suspend fun sync()
    suspend fun cancelLocal(operation: SyncOperation)
    suspend fun clearLocalState()
}
