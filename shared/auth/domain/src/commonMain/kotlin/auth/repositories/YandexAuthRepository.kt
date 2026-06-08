package auth.repositories

interface YandexAuthRepository {
    suspend fun handleOAuthCallback(parameters: Map<String, String>): String
}
