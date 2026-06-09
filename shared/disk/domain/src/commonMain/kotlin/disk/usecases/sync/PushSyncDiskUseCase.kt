package disk.usecases.sync

import disk.repositories.DiskSyncRepository

class PushSyncDiskUseCase(
    private val diskSyncRepository: DiskSyncRepository,
) {
    suspend operator fun invoke() {
        diskSyncRepository.sync()
    }
}
