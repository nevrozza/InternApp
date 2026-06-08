package auth

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import auth.internal.OAuthInProcessWidget
import auth.internal.ProfileWidget
import auth.internal.UnauthorizedRow
import auth.mvi.AuthStore
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun AuthWidget(
    component: AuthComponent,
    modifier: Modifier = Modifier
) {
    val state by component.model.subscribeAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(component) {
        component.labels.collect { label ->
            when (label) {
                is AuthStore.Label.OpenYandexOAuth -> uriHandler.openUri(label.url)
            }
        }
    }

    Crossfade(targetState = state.status, modifier = modifier) { status ->
        when (status) {
            is AuthStore.Status.Authorized -> ProfileWidget(
                profile = status.profileData,
                onLogoutClick = component::onLogoutClicked
            )

            is AuthStore.Status.Error -> UnauthorizedRow(
                errorMsg = status.msg,
                onLoginClick = component::onYandexLoginClicked
            )

            AuthStore.Status.InProcess -> OAuthInProcessWidget(component::onCancelAuthorizationClicked)
            AuthStore.Status.Unauthorized -> UnauthorizedRow(onLoginClick = component::onYandexLoginClicked)
        }
    }
}