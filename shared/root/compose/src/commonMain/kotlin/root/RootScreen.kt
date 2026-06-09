package root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import disk.DiskScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
internal fun RootScreen(
    component: RootComponent
) {
    val stack by component.stack.subscribeAsState()
    Children(
        stack = stack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(),
            onBack = component::onBackClicked
        )
    ) {
        when (val child = it.instance) {
            is Root.Child.Disk -> DiskScreen(child.diskComponent)
        }
    }

}
