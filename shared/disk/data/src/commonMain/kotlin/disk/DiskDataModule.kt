package disk

import disk.database.DiskDatabaseDataSource
import disk.network.DiskRemoteDataSource
import org.koin.dsl.module

val diskDataModule = module {
    single<DiskDatabaseDataSource> {
        DiskDatabaseDataSource(
            resourceDao = get(),
            syncOperationDao = get(),
        )
    }
    single<DiskRemoteDataSource> { DiskRemoteDataSource(get()) }
}
