package di

import auth.authModule
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import core.common.PlatformConfig
import core.storage.impl.coreStorageModule
import core.network.coreNetworkModule
import disk.diskDataModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(
    platformConfig: PlatformConfig,
    platformModules: List<Module> = emptyList(),
): KoinApplication {
    return startKoin {
        modules(
            listOf(
                module {
                    single { platformConfig }
                    single<StoreFactory> { LoggingStoreFactory(DefaultStoreFactory()) }
                },

                coreNetworkModule,
                coreStorageModule,

                authModule,
                diskDataModule,
            ) + platformModules
        )
    }
}
