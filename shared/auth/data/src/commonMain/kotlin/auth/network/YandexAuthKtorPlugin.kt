package auth.network

import auth.models.AuthEvent
import auth.repositories.AuthManager
import auth.storage.AuthTokenStorage
import core.network.api.KtorClientPlugin
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth

class YandexAuthKtorPlugin(
    private val tokenStorage: AuthTokenStorage,
    private val authManager: AuthManager,
    private val authRemoteDataSource: AuthRemoteDataSource,
) : KtorClientPlugin {
    override fun install(config: HttpClientConfig<*>) {
        config.install(Auth) {
            oauth {
                loadTokens {
                    val accessToken = tokenStorage.getAccessToken()
                    val refreshToken = tokenStorage.getRefreshToken()

                    if (accessToken != null && refreshToken != null) {
                        OAuthTokens(accessToken, refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val refreshToken = tokenStorage.getRefreshToken()
                        ?: return@refreshTokens null

                    runCatching {
                        authRemoteDataSource.refreshToken(refreshToken)
                    }.fold(
                        onSuccess = { tokens ->
                            tokenStorage.saveTokens(tokens)
                            OAuthTokens(
                                accessToken = tokens.accessToken,
                                refreshToken = tokens.refreshToken
                            )
                        },
                        onFailure = {
                            tokenStorage.clear()
                            authManager.refreshAuthState()
                            authManager.sendEvent(AuthEvent.Error("Session expired"))
                            null
                        }
                    )
                }

                sendWithoutRequest { request ->
                    request.url.host != "oauth.yandex.ru"
                }
            }
        }
    }
}
