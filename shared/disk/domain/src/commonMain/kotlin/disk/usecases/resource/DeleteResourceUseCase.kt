package disk.usecases.resource

import disk.repositories.DiskRepository
import utils.types.DiskPath

class DeleteResourceUseCase(
    private val diskRepository: DiskRepository,
) {
    suspend operator fun invoke(path: DiskPath) {
        diskRepository.delete(path)
    }
}
