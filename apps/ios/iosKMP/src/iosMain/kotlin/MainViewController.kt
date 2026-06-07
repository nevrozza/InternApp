package org.nevrzq.intern

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import core.common.PlatformConfig
import platform.UIKit.UIViewController
import di.initKoin
import root.RealRootComponent
import root.RootComponent
import root.RootScreen

@Suppress("unused", "FunctionName")
fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        initKoin(
            PlatformConfig()
        )

        val rootComponent: RootComponent = RealRootComponent(
            DefaultComponentContext(
                ApplicationLifecycle()
            )
        )

        RootScreen(rootComponent)
    }
}