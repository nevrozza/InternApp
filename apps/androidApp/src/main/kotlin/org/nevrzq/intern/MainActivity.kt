package org.nevrzq.intern

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import auth.repositories.YandexOAuthRedirectUriProvider
import com.arkivanov.decompose.retainedComponent
import core.common.PlatformConfig
import di.initKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import root.RealRootComponent
import root.RootComponent
import App
import utils.config.AppConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        initKoin(
            platformConfig = PlatformConfig(context = applicationContext),
            platformModules = listOf(
                module {
                    single<YandexOAuthRedirectUriProvider> {
                        YandexOAuthRedirectUriProvider { AppConfig.YandexOAuthConfig.Mobile.redirectUri }
                    }
                }
            )
        )

        handleOAuthIntent(lifecycleScope, intent)

        val rootComponent: RootComponent = retainedComponent { componentContext ->
            RealRootComponent(componentContext)
        }
        screenSetup()

        setContent {
            App(rootComponent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleOAuthIntent(lifecycleScope, intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }


}
