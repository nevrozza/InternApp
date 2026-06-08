package auth.network

import auth.network.dto.YandexTokenResponse
import auth.network.dto.YandexUserInfoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
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
            url = AuthNetworkPaths.EXCHANGE_CODE,
            formParameters = Parameters.build {
                append("grant_type", "authorization_code")
                append("code", code)
                append("client_id", AppConfig.YandexOAuthConfig.clientId)
                append("code_verifier", codeVerifier)
            }
        ).body()
    }

    suspend fun getUserInfo(): YandexUserInfoResponse {
        return hc.get(AuthNetworkPaths.GET_USER_INFO) {
            url {
                parameters.append("format", "json")
            }
        }.body()
    }
}
