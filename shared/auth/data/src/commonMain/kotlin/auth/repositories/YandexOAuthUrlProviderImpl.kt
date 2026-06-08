package auth.repositories

import auth.storage.AuthTokenStorage
import io.ktor.http.URLBuilder
import io.ktor.util.generateNonce
import io.ktor.utils.io.InternalAPI
import utils.config.AppConfig
import utils.crypto.Sha256
import kotlin.io.encoding.Base64

internal class YandexOAuthUrlProviderImpl(
    private val redirectUriProvider: YandexOAuthRedirectUriProvider,
    private val tokenStorage: AuthTokenStorage,
) : YandexOAuthUrlProvider {
    override suspend fun getUrl(): String {
        val redirectUri = redirectUriProvider.getRedirectUri()
        val codeVerifier = createCodeVerifier()
        tokenStorage.saveCodeVerifier(codeVerifier)

        return URLBuilder("https://oauth.yandex.ru/authorize").apply {
            parameters.append("response_type", "code")
            parameters.append("client_id", AppConfig.YandexOAuthConfig.clientId)
            parameters.append("redirect_uri", redirectUri)
            parameters.append("scope", AppConfig.YandexOAuthConfig.scope)
            parameters.append("code_challenge", createCodeChallenge(codeVerifier))
            parameters.append("code_challenge_method", "S256")
            parameters.append("force_confirm", "yes")
        }.buildString()
    }

    private fun createCodeVerifier(): String =
        generateNonce(size = CODE_VERIFIER_BYTES)
            .base64Url()
            .take(CODE_VERIFIER_LENGTH)


    @OptIn(InternalAPI::class)
    private fun createCodeChallenge(codeVerifier: String): String =
        Sha256
            .digest(codeVerifier.encodeToByteArray())
            .base64Url()

    private fun ByteArray.base64Url(): String =
        Base64.encode(this)
            .replace('+', '-')
            .replace('/', '_')
            .trimEnd('=')

    private companion object {
        const val CODE_VERIFIER_BYTES = 64
        const val CODE_VERIFIER_LENGTH = 64
    }
}
