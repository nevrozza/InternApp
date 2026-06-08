package auth.usecases

import auth.repositories.AuthManager

class RefreshAuthStateUseCase(
    private val authManager: AuthManager,
) {
    operator fun invoke() {
        authManager.refreshAuthState()
    }
}