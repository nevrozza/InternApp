package root

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootScreen(
    component: RootComponent
) {
    val stack by component.stack.subscribeAsState()

    Surface {
        Children(
            stack = stack,
            animation = predictiveBackAnimation(
                backHandler = component.backHandler,
                fallbackAnimation = stackAnimation(),
                onBack = component::onBackClicked
            )
        ) {
            when (val child = it.instance) {
                is Root.Child.Files -> Text("Files")
                is Root.Child.Settings -> Text("Settings")
            }
        }
    }
}