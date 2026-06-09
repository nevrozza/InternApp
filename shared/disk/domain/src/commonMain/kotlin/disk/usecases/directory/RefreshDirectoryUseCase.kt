package disk.usecases.directory

import disk.repositories.DiskRepository
import utils.types.DiskPath

class RefreshDirectoryUseCase(
    private val diskRepository: DiskRepository,
) {
    suspend operator fun invoke(path: DiskPath) {
        diskRepository.refreshDirectory(path)
    }
}
