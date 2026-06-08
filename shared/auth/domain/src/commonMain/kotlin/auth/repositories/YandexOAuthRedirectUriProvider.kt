package auth.repositories

fun interface YandexOAuthRedirectUriProvider {
    fun getRedirectUri(): String
}
