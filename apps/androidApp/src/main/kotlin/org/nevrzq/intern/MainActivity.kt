package org.nevrzq.intern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.retainedComponent
import core.common.PlatformConfig
import org.koin.core.context.GlobalContext.stopKoin
import root.RealRootComponent
import root.RootScreen
import root.RootComponent
import di.initKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        initKoin(
            PlatformConfig(context = applicationContext)
        )

        val rootComponent: RootComponent = retainedComponent { componentContext ->
            RealRootComponent(componentContext)
        }
        screenSetup()

        setContent {
            RootScreen(rootComponent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}