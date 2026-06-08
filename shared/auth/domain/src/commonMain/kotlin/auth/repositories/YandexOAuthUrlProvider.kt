package auth.repositories

fun interface YandexOAuthUrlProvider {
    suspend fun getUrl(): String?
}
