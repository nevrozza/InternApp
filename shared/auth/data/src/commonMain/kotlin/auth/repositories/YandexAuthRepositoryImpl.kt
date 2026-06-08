package auth.repositories

import auth.models.AuthEvent
import auth.storage.AuthTokenStorage

class YandexAuthRepositoryImpl(
    private val tokenStorage: AuthTokenStorage,
    private val authManager: AuthManager
) : YandexAuthRepository {
    override suspend fun handleOAuthCallback(parameters: Map<String, String>): String {

        // https://yandex.ru/dev/id/doc/en/codes/code-url#code-response
        val error = parameters["error"]
        if (error != null) {
            val description = parameters["error_description"] ?: error
            authManager.sendEvent(AuthEvent.Error(description))
            return "OAuth error: $description"
        }

        val code = parameters["code"]
        if (code == null) {
            val description = "OAuth callback does not contain code"
            authManager.sendEvent(AuthEvent.Error(description))
            return description
        }

        println("CODE: $code")

        oauthCallback(
            code
        )

        return "OAuth callback received. You can close this tab."
    }

    private suspend fun oauthCallback(code: String) {
        // TODO: exchange callback.code for tokens, then save tokens and refresh auth state.
        authManager.refreshAuthState()
    }
}
