package core.storage.impl.room

import core.common.PlatformConfig
import org.koin.dsl.module

internal val roomModule = module {
    single<AppDatabase> {
        createAppDatabase(
            builder = getAppDatabaseBuilder(
                platformConfig = get<PlatformConfig>(),
            ),
        )
    }

    single { get<AppDatabase>().diskResourceDao() }
    single { get<AppDatabase>().diskSyncOperationDao() }
}
