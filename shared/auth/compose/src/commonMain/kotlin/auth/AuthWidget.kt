package auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import auth.internal.ProfileWidget
import auth.mvi.AuthStore
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun AuthWidget(
    component: AuthComponent
) {
    val state by component.model.subscribeAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(component) {
        component.labels.collect { label ->
            when (label) {
                is AuthStore.Label.OpenYandexOAuth -> uriHandler.openUri(label.url)
                AuthStore.Label.CloseOAuth -> Unit
            }
        }
    }

    when (val status = state.status) {
        is AuthStore.Status.Authorized -> ProfileWidget(
            profile = status.profileData,
            onLogoutClick = component::onLogoutClicked
        )

        is AuthStore.Status.Error -> TODO()
        AuthStore.Status.InProcess -> {}
        AuthStore.Status.Unauthorized -> {}
    }
}