package auth.repositories

fun interface YandexOAuthUrlProvider {
    fun getUrl(): String?
}
