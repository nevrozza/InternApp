package auth.models

sealed interface AuthState {
    data object Authorized : AuthState
    data object Unauthorized : AuthState
}