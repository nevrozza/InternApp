package auth.repositories

import auth.models.AuthState
import kotlinx.coroutines.flow.StateFlow


interface AuthManager {
    val authState: StateFlow<AuthState>

    fun refreshAuthState()
    fun logout()
}