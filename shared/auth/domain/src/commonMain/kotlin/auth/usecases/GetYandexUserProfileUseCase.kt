package auth.usecases

import auth.repositories.YandexAuthRepository

class GetYandexUserProfileUseCase(
    private val yandexAuthRepository: YandexAuthRepository,
) {
    operator fun invoke() = yandexAuthRepository.getUserProfile()
}
