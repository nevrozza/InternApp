package disk.usecases

import disk.usecases.directory.CreateFolderUseCase
import disk.usecases.directory.ObserveDirectoryUseCase
import disk.usecases.directory.RefreshDirectoryUseCase
import disk.usecases.resource.DeleteResourceUseCase
import disk.usecases.resource.RenameResourceUseCase
import disk.usecases.resource.UploadFileUseCase
import disk.usecases.sync.ObserveSyncOperationsUseCase
import disk.usecases.sync.PushSyncDiskUseCase
import disk.usecases.text.SaveTextFileUseCase

data class DiskUseCases(
    val observeDirectory: ObserveDirectoryUseCase,
    val refreshDirectory: RefreshDirectoryUseCase,
    val createFolder: CreateFolderUseCase,
    val deleteResource: DeleteResourceUseCase,
    val renameResource: RenameResourceUseCase,
    val saveTextFile: SaveTextFileUseCase,
    val uploadFile: UploadFileUseCase,
    val observeSyncOperations: ObserveSyncOperationsUseCase,
    val pushSyncDisk: PushSyncDiskUseCase,
)
