package disk.usecases.directory

import disk.models.resources.DiskResource
import disk.repositories.DiskRepository
import kotlinx.coroutines.flow.Flow
import utils.types.DiskPath

class ObserveDirectoryUseCase(
    private val diskRepository: DiskRepository,
) {
    operator fun invoke(path: DiskPath): Flow<List<DiskResource>> {
        return diskRepository.observeDirectory(path)
    }
}
