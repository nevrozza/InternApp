package auth.storage

import core.storage.keyvalue.EncryptedKeyValueStorage
import core.storage.keyvalue.PlainKeyValueStorage

class AuthTokenStorage(
    private val encrypted: EncryptedKeyValueStorage,
    private val kv: PlainKeyValueStorage
) {
    private val keys =
        OAuthStorageKeys("yandex") // В будущем можно будет добавить других провайдеров

    fun saveTokens(data: Any) {

    }

    fun getTokens(): Any? = null

    fun getAccessToken(): Any? = null

    fun saveProfile(data: Any) {

    }

    fun getProfile(): Any? = null

    fun clear() {
    }
}