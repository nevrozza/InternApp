package disk

import disk.database.DiskDatabaseDataSource
import disk.network.DiskRemoteDataSource
import disk.repositories.DiskRepository
import disk.repositories.DiskRepositoryImpl
import disk.repositories.DiskSyncRepository
import disk.repositories.DiskSyncRepositoryImpl
import disk.usecases.DiskUseCases
import disk.usecases.directory.CreateFolderUseCase
import disk.usecases.directory.ObserveDirectoryUseCase
import disk.usecases.directory.RefreshDirectoryUseCase
import disk.usecases.resource.DeleteResourceUseCase
import disk.usecases.resource.RenameResourceUseCase
import disk.usecases.resource.UploadFileUseCase
import disk.usecases.sync.CancelLocalSyncUseCase
import disk.usecases.sync.ObserveSyncOperationsUseCase
import disk.usecases.sync.PushSyncDiskUseCase
import disk.usecases.text.SaveTextFileUseCase
import org.koin.dsl.module

val diskDataModule = module {
    single<DiskDatabaseDataSource> {
        DiskDatabaseDataSource(
            database = get(),
            resourceDao = get(),
            syncOperationDao = get(),
        )
    }
    single<DiskRemoteDataSource> { DiskRemoteDataSource(get()) }
    single<DiskRepository> { DiskRepositoryImpl(get(), get()) }
    single<DiskSyncRepository> { DiskSyncRepositoryImpl(get(), get()) }

    factory { ObserveDirectoryUseCase(get()) }
    factory { RefreshDirectoryUseCase(get()) }
    factory { CreateFolderUseCase(get()) }
    factory { DeleteResourceUseCase(get()) }
    factory { RenameResourceUseCase(get()) }
    factory { SaveTextFileUseCase(get()) }
    factory { UploadFileUseCase(get()) }
    factory { ObserveSyncOperationsUseCase(get()) }
    factory { PushSyncDiskUseCase(get()) }
    factory { CancelLocalSyncUseCase(get()) }

    factory {
        DiskUseCases(
            observeDirectory = get(),
            refreshDirectory = get(),
            createFolder = get(),
            deleteResource = get(),
            renameResource = get(),
            saveTextFile = get(),
            uploadFile = get(),
            observeSyncOperations = get(),
            pushSyncDisk = get(),
            cancelLocalSync = get(),
        )
    }
}
