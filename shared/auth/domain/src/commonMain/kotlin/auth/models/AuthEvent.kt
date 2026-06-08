package auth.models

sealed interface AuthEvent {
    data class Error(val msg: String?) : AuthEvent
}
