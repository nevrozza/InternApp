package auth.repositories

import auth.models.YandexOAuthLoginData

interface YandexAuthRepository {
    fun oauthDeepLink(data: YandexOAuthLoginData)
}