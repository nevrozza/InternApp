package auth.usecases

import auth.models.AuthState
import auth.repositories.AuthManager
import kotlinx.coroutines.flow.StateFlow

class ObserveAuthStateUseCase(
    private val authManager: AuthManager,
) {
    operator fun invoke(): StateFlow<AuthState> {
        return authManager.authState
    }
}