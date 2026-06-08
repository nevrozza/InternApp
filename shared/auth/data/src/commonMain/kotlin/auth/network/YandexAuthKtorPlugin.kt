package auth.network

import auth.models.AuthEvent
import auth.network.dto.YandexTokenResponse
import auth.repositories.AuthManager
import auth.storage.AuthTokenStorage
import core.network.api.KtorClientPlugin
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import utils.config.AppConfig

class YandexAuthKtorPlugin(
    private val tokenStorage: AuthTokenStorage,
    private val authManager: AuthManager,
    private val authHc: HttpClient
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
                        authHc.submitForm(
                            url = "https://oauth.yandex.ru/token",
                            formParameters = Parameters.build {
                                append("grant_type", "refresh_token")
                                append("client_id", AppConfig.YandexOAuthConfig.clientId)
                                append("refresh_token", refreshToken)
                            }
                        ).body<YandexTokenResponse>()
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
