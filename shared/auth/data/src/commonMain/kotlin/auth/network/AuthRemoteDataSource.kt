package auth.network

import auth.network.dto.YandexTokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import utils.config.AppConfig

class AuthRemoteDataSource(
    private val hc: HttpClient
) {

    suspend fun refreshToken(refreshToken: String): YandexTokenResponse {
        return hc.submitForm(
            url = "https://oauth.yandex.ru/token",
            formParameters = Parameters.build {
                append("grant_type", "refresh_token")
                append("client_id", AppConfig.YandexOAuthConfig.clientId)
                append("refresh_token", refreshToken)
            }
        ).body<YandexTokenResponse>()
    }

    suspend fun exchangeCode(
        code: String,
        codeVerifier: String,
    ): YandexTokenResponse {
        return hc.submitForm(
            url = "https://oauth.yandex.ru/token",
            formParameters = Parameters.build {
                append("grant_type", "authorization_code")
                append("code", code)
                append("client_id", AppConfig.YandexOAuthConfig.clientId)
                append("code_verifier", codeVerifier)
            }
        ).body()
    }
}