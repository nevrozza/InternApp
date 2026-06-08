import androidx.compose.ui.window.ComposeUIViewController
import auth.repositories.YandexOAuthRedirectUriProvider
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import core.common.PlatformConfig
import platform.UIKit.UIViewController
import di.initKoin
import org.koin.dsl.module
import root.RealRootComponent
import root.RootComponent
import utils.config.AppConfig

@Suppress("unused", "FunctionName")
fun MainViewController(): UIViewController {

    initKoin(
        platformConfig = PlatformConfig(),
        platformModules = listOf(
            module {
                single<YandexOAuthRedirectUriProvider> {
                    YandexOAuthRedirectUriProvider { AppConfig.YandexOAuthConfig.Mobile.redirectUri }
                }
            }
        )
    )

    val rootComponent: RootComponent = RealRootComponent(
        DefaultComponentContext(
            ApplicationLifecycle()
        )
    )

    return ComposeUIViewController {

        App(rootComponent)
    }
}
