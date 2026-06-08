package auth

import auth.repositories.AuthManager
import auth.repositories.AuthManagerImpl
import auth.repositories.YandexAuthRepository
import auth.repositories.YandexAuthRepositoryImpl
import auth.desktopServer.YandexOAuthCallbackServer
import auth.storage.AuthTokenStorage
import auth.usecases.LogoutUseCase
import auth.usecases.ObserveAuthEventsUseCase
import auth.usecases.ObserveAuthStateUseCase
import auth.usecases.RefreshAuthStateUseCase
import auth.desktopServer.StartYandexOAuthCallbackServerUseCase
import auth.desktopServer.StopYandexOAuthCallbackServerUseCase
import org.koin.dsl.module

val authModule = module {
    single<AuthTokenStorage> { AuthTokenStorage(get(), get()) }


    single<AuthManager> { AuthManagerImpl(get()) }
    single<YandexAuthRepository> { YandexAuthRepositoryImpl(get(), get()) }


    factory { ObserveAuthStateUseCase(get()) }
    factory { ObserveAuthEventsUseCase(get()) }
    factory { RefreshAuthStateUseCase(get()) }
    factory { StartYandexOAuthCallbackServerUseCase(getOrNull() ?: NoOpYandexOAuthCallbackServer) }
    factory { StopYandexOAuthCallbackServerUseCase(getOrNull() ?: NoOpYandexOAuthCallbackServer) }
    factory { LogoutUseCase(get()) }
}

private object NoOpYandexOAuthCallbackServer : YandexOAuthCallbackServer {
    override fun start() = Unit
    override fun stop() = Unit
}
