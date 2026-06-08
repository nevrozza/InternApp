package auth.repositories

import auth.models.AuthEvent
import auth.models.AuthState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


interface AuthManager {
    val authState: StateFlow<AuthState>
    val authEvents: SharedFlow<AuthEvent>

    suspend fun sendEvent(event: AuthEvent)
    fun refreshAuthState()
    fun logout()
}
