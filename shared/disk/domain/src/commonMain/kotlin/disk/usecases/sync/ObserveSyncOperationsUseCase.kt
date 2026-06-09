package disk.usecases.sync

import disk.models.sync.SyncOperation
import disk.repositories.DiskSyncRepository
import kotlinx.coroutines.flow.Flow

class ObserveSyncOperationsUseCase(
    private val diskSyncRepository: DiskSyncRepository,
) {
    operator fun invoke(): Flow<List<SyncOperation>> {
        return diskSyncRepository.observeOperations()
    }
}
