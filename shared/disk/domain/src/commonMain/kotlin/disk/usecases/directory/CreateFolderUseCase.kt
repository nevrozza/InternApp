package disk.usecases.directory

import disk.repositories.DiskRepository
import utils.types.DiskPath

class CreateFolderUseCase(
    private val diskRepository: DiskRepository,
) {
    suspend operator fun invoke(
        parentPath: DiskPath,
        name: String,
    ) {
        diskRepository.createFolder(
            parentPath = parentPath,
            name = name,
        )
    }
}
