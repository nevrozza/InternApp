package org.nevrzq.intern

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import auth.repositories.YandexAuthRepository
import auth.repositories.YandexOAuthUrlProvider
import com.arkivanov.decompose.retainedComponent
import core.common.PlatformConfig
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.GlobalContext.get
import root.RealRootComponent
import root.RootScreen
import root.RootComponent
import di.initKoin
import org.koin.dsl.module
import utils.config.AppConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        initKoin(
            platformConfig = PlatformConfig(context = applicationContext),
            platformModules = listOf(
                module {
                    single<YandexOAuthUrlProvider> {
                        YandexOAuthUrlProvider {
                            AppConfig.YandexOAuthConfig.Android.authUrl
                        }
                    }
                }
            )
        )

        handleOAuthIntent(intent)

        val rootComponent: RootComponent = retainedComponent { componentContext ->
            RealRootComponent(componentContext)
        }
        screenSetup()

        setContent {
            RootScreen(rootComponent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleOAuthIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

    private fun handleOAuthIntent(intent: Intent?) {
        val uri = intent?.data ?: return
        if (uri.scheme != "org.nevrzq.intern") return
        if (uri.host != "oauth") return
        if (uri.path != "/yandex/callback") return

        val parameters = uri.queryParameterNames.associateWith { name ->
            uri.getQueryParameter(name).orEmpty()
        }

        lifecycleScope.launch {
            get().get<YandexAuthRepository>().handleOAuthCallback(parameters)
        }
    }
}
