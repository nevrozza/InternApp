package auth.repositories

import auth.models.AuthEvent
import auth.models.YandexUserProfile
import auth.network.AuthRemoteDataSource
import auth.network.dto.toDomain
import auth.storage.AuthTokenStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class YandexAuthRepositoryImpl(
    private val tokenStorage: AuthTokenStorage,
    private val authManager: AuthManager,
    private val authRemoteDataSource: AuthRemoteDataSource,
) : YandexAuthRepository {
    private val profileSyncMutex = Mutex()
    private var wasProfileSyncedInCurrentSession: Boolean = false

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

        val codeVerifier = tokenStorage.takeCodeVerifier()
        if (codeVerifier == null) {
            val description = "OAuth code verifier is missing"
            authManager.sendEvent(AuthEvent.Error(description))
            return description
        }

        runCatching {
            oauthCallback(
                code = code,
                codeVerifier = codeVerifier,
            )
        }.onFailure { error ->
            val description = error.message ?: "OAuth token exchange failed"
            authManager.sendEvent(AuthEvent.Error(description))
            return description
        }

        return "OAuth callback received. You can close this tab."
    }

    override fun getUserProfile(): Flow<YandexUserProfile> = flow {
        val cachedProfile = tokenStorage.getProfile()
        if (cachedProfile != null) {
            emit(cachedProfile)
        }

        profileSyncMutex.withLock {
            if (wasProfileSyncedInCurrentSession) {
                return@flow
            }

            runCatching {
                authRemoteDataSource.getUserInfo()
                    .toDomain()
                    .also(tokenStorage::saveProfile)
            }.onSuccess { freshProfile ->
                wasProfileSyncedInCurrentSession = true
                if (freshProfile != cachedProfile) {
                    emit(freshProfile)
                }
            }.onFailure { error ->
                if (cachedProfile == null) {
                    throw error
                }
            }
        }
    }

    private suspend fun oauthCallback(code: String, codeVerifier: String) {
        val tokens = authRemoteDataSource.exchangeCode(
            code = code,
            codeVerifier = codeVerifier,
        )

        tokenStorage.saveTokens(tokens)
        tokenStorage.clearProfile()
        wasProfileSyncedInCurrentSession = false
        authManager.refreshAuthState()
    }
}
