package auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import auth.mvi.AuthStore
import com.arkivanov.decompose.extensions.compose.subscribeAsState

// AI Generated
@Composable
fun AuthDebugContent(
    component: AuthComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState()
    val labels = remember { mutableStateListOf<String>() }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(component) {
        component.labels.collect { label ->
            labels += label.toString()
            when (label) {
                is AuthStore.Label.OpenYandexOAuth -> uriHandler.openUri(label.url)
                AuthStore.Label.CloseOAuth -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Auth debug",
            style = MaterialTheme.typography.titleMedium,
        )

        Text("Status: ${state.status.debugName()}")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = component::onYandexLoginClicked) {
                Text("Login")
            }

            OutlinedButton(onClick = component::onLogoutClicked) {
                Text("Logout")
            }

            OutlinedButton(onClick = component::onCancelAuthorizationClicked) {
                Text("Cancel")
            }
        }

        Text(
            text = "Labels:",
            style = MaterialTheme.typography.titleSmall,
        )

        if (labels.isEmpty()) {
            Text("none")
        } else {
            labels.takeLast(8).forEach { label ->
                Text(label)
            }
        }
    }
}

private fun AuthStore.Status.debugName(): String =
    when (this) {
        is AuthStore.Status.Authorized -> "Authorized(profileData=$profileData)"
        AuthStore.Status.Unauthorized -> "Unauthorized"
        AuthStore.Status.InProcess -> "InProcess"
        is AuthStore.Status.Error -> "Error(msg=$msg)"
    }
