package root

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import auth.AuthWidget
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import utils.compose.consts.Paddings

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
            is Root.Child.Files -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(Modifier.fillMaxSize().safeDrawingPadding()) {
                        AuthWidget(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = Paddings.medium, vertical = Paddings.semiBig)
                                .clip(MaterialTheme.shapes.extraLarge)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .padding(horizontal = Paddings.medium, vertical = Paddings.semiBig).animateContentSize(),
                            component = child.authComponent
                        )
                        Text("Files")
                    }
                }
            }
        }
    }

}