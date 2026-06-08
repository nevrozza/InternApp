package auth.repositories

import auth.models.YandexUserProfile

interface YandexAuthRepository {
    suspend fun handleOAuthCallback(parameters: Map<String, String>): String
    suspend fun getUserProfile(): YandexUserProfile
}
