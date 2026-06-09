package disk.usecases.sync

import disk.repositories.DiskSyncRepository

class ClearDiskCacheUseCase(
    private val diskSyncRepository: DiskSyncRepository,
) {
    suspend operator fun invoke() {
        diskSyncRepository.clearLocalState()
    }
}
