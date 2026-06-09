package disk.usecases.sync

import disk.models.sync.SyncOperation
import disk.repositories.DiskSyncRepository

class CancelLocalSyncUseCase(
    private val diskSyncRepository: DiskSyncRepository,
) {
    suspend operator fun invoke(operation: SyncOperation) {
        diskSyncRepository.cancelLocal(operation)
    }
}
