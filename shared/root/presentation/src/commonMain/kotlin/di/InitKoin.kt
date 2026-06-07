package di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import core.common.PlatformConfig
import core.storage.impl.coreStorageModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(
    platformConfig: PlatformConfig
): KoinApplication {
    return startKoin {
        module {
            single { platformConfig }


            single<StoreFactory> { LoggingStoreFactory(DefaultStoreFactory()) }
        }

        modules(
            coreStorageModule
        )
    }
}