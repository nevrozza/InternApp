package auth.network

import auth.network.dto.YandexTokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import utils.config.AppConfig

class AuthRemoteDataSource(
    private val hc: HttpClient,
    private val authHc: HttpClient
) {
    suspend fun exchangeCode(
        code: String,
        codeVerifier: String,
    ): YandexTokenResponse {
        return authHc.submitForm(
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
