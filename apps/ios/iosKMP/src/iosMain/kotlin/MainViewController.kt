package org.nevrzq.intern

import androidx.compose.ui.window.ComposeUIViewController
import auth.repositories.YandexOAuthUrlProvider
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import core.common.PlatformConfig
import platform.UIKit.UIViewController
import di.initKoin
import org.koin.dsl.module
import root.RealRootComponent
import root.RootComponent
import root.RootScreen
import utils.config.AppConfig

@Suppress("unused", "FunctionName")
fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        initKoin(
            platformConfig = PlatformConfig(),
            platformModules = listOf(
                module {
                    single<YandexOAuthUrlProvider> {
                        // афигеть синтаксис
                        YandexOAuthUrlProvider { AppConfig.YandexOAuthConfig.Mobile.authUrl }
                    }
                }
            )
        )

        val rootComponent: RootComponent = RealRootComponent(
            DefaultComponentContext(
                ApplicationLifecycle()
            )
        )

        RootScreen(rootComponent)
    }
}
