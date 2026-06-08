package auth.usecases

import auth.repositories.YandexAuthRepository

class GetYandexUserProfileUseCase(
    private val yandexAuthRepository: YandexAuthRepository,
) {
    suspend operator fun invoke() = yandexAuthRepository.getUserProfile()
}
