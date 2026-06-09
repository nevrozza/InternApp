package disk

import disk.database.DiskDatabaseDataSource
import disk.network.DiskRemoteDataSource
import disk.repositories.DiskRepository
import disk.repositories.DiskRepositoryImpl
import disk.repositories.DiskSyncRepository
import disk.repositories.DiskSyncRepositoryImpl
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
}
