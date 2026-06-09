package disk.usecases.resource

import disk.repositories.DiskRepository
import utils.types.DiskPath
import utils.types.LocalPath

class UploadFileUseCase(
    private val diskRepository: DiskRepository,
) {
    suspend operator fun invoke(
        parentPath: DiskPath,
        name: String,
        localFilePath: LocalPath,
    ) {
        diskRepository.uploadFile(
            parentPath = parentPath,
            name = name,
            localFilePath = localFilePath,
        )
    }
}
