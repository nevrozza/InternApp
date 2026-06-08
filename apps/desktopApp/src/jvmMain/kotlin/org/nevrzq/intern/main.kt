package org.nevrzq.intern

import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import core.common.PlatformConfig
import di.initKoin
import root.RealRootComponent
import App
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import org.nevrzq.intern.auth.desktopAuthCallbackServerModule

fun main() {

    initKoin(
        platformConfig = PlatformConfig(),
        platformModules = listOf(desktopAuthCallbackServerModule),
    )

    val lifecycle = LifecycleRegistry()

    val root = runOnUiThread {
        RealRootComponent(componentContext = DefaultComponentContext(lifecycle))
    }

    application {
        val windowState = rememberWindowState()

        Window(
            onCloseRequest = ::exitApplication,
            title = "InternApp",
            state = windowState
        ) {
            LifecycleController(
                lifecycleRegistry = lifecycle,
                windowState = windowState,
                windowInfo = LocalWindowInfo.current,
            )
            App(root)
        }
    }
}
