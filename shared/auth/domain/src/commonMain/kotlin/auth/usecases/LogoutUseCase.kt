package auth.usecases

import auth.repositories.AuthManager

class LogoutUseCase(
    private val authManager: AuthManager,
) {
    operator fun invoke() {
        authManager.logout()
    }
}