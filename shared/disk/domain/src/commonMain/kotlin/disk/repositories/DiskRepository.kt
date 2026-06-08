package disk.repositories

import disk.models.DiskPath
import disk.models.LocalPath
import disk.models.resources.DiskResource
import kotlinx.coroutines.flow.Flow

interface DiskRepository {

    fun observeDirectory(path: DiskPath): Flow<List<DiskResource>>

    suspend fun refreshDirectory(path: DiskPath)

    suspend fun getResource(path: DiskPath): DiskResource?

    suspend fun createFolder(parentPath: DiskPath, name: String)

    suspend fun delete(path: DiskPath)

    suspend fun rename(
        sourcePath: DiskPath,
        newName: String,
    )

    suspend fun saveTextFile(
        path: DiskPath,
        content: String,
    )

    suspend fun uploadFile(
        parentPath: DiskPath,
        name: String,
        localFilePath: LocalPath,
    )
}