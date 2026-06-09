package disk.usecases.resource

import disk.repositories.DiskRepository
import utils.types.DiskPath

class RenameResourceUseCase(
    private val diskRepository: DiskRepository,
) {
    suspend operator fun invoke(
        sourcePath: DiskPath,
        newName: String,
    ) {
        diskRepository.rename(
            sourcePath = sourcePath,
            newName = newName,
        )
    }
}
