package auth

import auth.mvi.AuthStore
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

interface AuthComponent {
    val model: Value<AuthStore.State>
    val labels: Flow<AuthStore.Label>

    fun onYandexLoginClicked()
    fun onLogoutClicked()
    fun onCancelAuthorizationClicked()
}
