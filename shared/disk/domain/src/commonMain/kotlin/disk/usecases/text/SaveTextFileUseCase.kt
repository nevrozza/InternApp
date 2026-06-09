package disk.usecases.text

import disk.repositories.DiskRepository
import utils.types.DiskPath

class SaveTextFileUseCase(
    private val diskRepository: DiskRepository,
) {
    suspend operator fun invoke(
        path: DiskPath,
        content: String,
    ) {
        diskRepository.saveTextFile(
            path = path,
            content = content,
        )
    }
}
