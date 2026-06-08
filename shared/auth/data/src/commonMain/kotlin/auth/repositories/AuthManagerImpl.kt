package auth.repositories

import auth.models.AuthEvent
import auth.models.AuthState
import auth.storage.AuthTokenStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthManagerImpl(
    private val tokenStorage: AuthTokenStorage
) : AuthManager {
    override val authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Unauthorized)
    override val authEvents: MutableSharedFlow<AuthEvent> = MutableSharedFlow(extraBufferCapacity = 1)

    override suspend fun sendEvent(event: AuthEvent) {
        authEvents.emit(event)
    }

    override fun refreshAuthState() {
        authState.value = if (tokenStorage.getAccessToken() != null) {
            AuthState.Authorized
        } else {
            AuthState.Unauthorized
        }
    }

    override fun logout() {
        tokenStorage.clear()
        authState.value = AuthState.Unauthorized
    }
}
