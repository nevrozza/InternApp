package auth.repositories

import auth.models.YandexUserProfile
import kotlinx.coroutines.flow.Flow

interface YandexAuthRepository {
    suspend fun handleOAuthCallback(parameters: Map<String, String>): String
    fun getUserProfile(): Flow<YandexUserProfile>
}
