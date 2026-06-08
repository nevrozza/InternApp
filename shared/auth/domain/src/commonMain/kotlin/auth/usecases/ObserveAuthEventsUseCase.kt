package auth.usecases

import auth.models.AuthEvent
import auth.repositories.AuthManager
import kotlinx.coroutines.flow.SharedFlow

class ObserveAuthEventsUseCase(
    private val authManager: AuthManager,
) {
    operator fun invoke(): SharedFlow<AuthEvent> {
        return authManager.authEvents
    }
}
