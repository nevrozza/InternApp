package auth.storage

import auth.network.dto.YandexTokenResponse
import core.storage.keyvalue.EncryptedKeyValueStorage
import core.storage.keyvalue.PlainKeyValueStorage
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AuthTokenStorage(
    private val encrypted: EncryptedKeyValueStorage,
    private val kv: PlainKeyValueStorage
) {
    private val keys =
        OAuthStorageKeys("yandex") // В будущем можно будет добавить других провайдеров

    @OptIn(ExperimentalTime::class)
    fun saveTokens(data: YandexTokenResponse) {
        encrypted[keys.accessToken] = data.accessToken
        encrypted[keys.refreshToken] = data.refreshToken
        kv[keys.tokenType] = data.tokenType
        kv[keys.expiresInSeconds] = data.expiresIn.toString()
        kv[keys.createdAtMillis] = Clock.System.now().toEpochMilliseconds().toString()
    }

    fun getAccessToken(): String? = encrypted[keys.accessToken]
    fun getRefreshToken(): String? = encrypted[keys.refreshToken]

    fun saveCodeVerifier(codeVerifier: String) {
        encrypted[keys.codeVerifier] = codeVerifier
    }

    fun takeCodeVerifier(): String? =
        encrypted[keys.codeVerifier]?.also {
            encrypted.remove(keys.codeVerifier)
        }

    fun saveProfile(data: Any) {

    }

    fun getProfile(): Any? = null

    fun clear() {
        encrypted.remove(keys.accessToken)
        encrypted.remove(keys.refreshToken)
        encrypted.remove(keys.codeVerifier)
        kv.remove(keys.tokenType)
        kv.remove(keys.expiresInSeconds)
        kv.remove(keys.createdAtMillis)
    }
}
