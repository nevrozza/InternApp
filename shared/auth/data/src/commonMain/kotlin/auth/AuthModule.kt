package auth

import auth.desktopServer.StartYandexOAuthCallbackServerUseCase
import auth.desktopServer.StopYandexOAuthCallbackServerUseCase
import auth.desktopServer.YandexOAuthCallbackServer
import auth.network.AuthRemoteDataSource
import auth.network.YandexAuthKtorPlugin
import auth.repositories.AuthManager
import auth.repositories.AuthManagerImpl
import auth.repositories.YandexAuthRepository
import auth.repositories.YandexAuthRepositoryImpl
import auth.repositories.YandexOAuthUrlProvider
import auth.repositories.YandexOAuthUrlProviderImpl
import auth.storage.AuthTokenStorage
import auth.usecases.GetYandexUserProfileUseCase
import auth.usecases.LogoutUseCase
import auth.usecases.ObserveAuthEventsUseCase
import auth.usecases.ObserveAuthStateUseCase
import auth.usecases.RefreshAuthStateUseCase
import core.network.api.KtorClientPlugin
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {

    single<KtorClientPlugin> { YandexAuthKtorPlugin(get(), get(), get(named("auth"))) }

    single<AuthTokenStorage> { AuthTokenStorage(get(), get()) }
    single<AuthRemoteDataSource> { AuthRemoteDataSource(get(), get(named("auth"))) }


    single<AuthManager> { AuthManagerImpl(get()) }
    single<YandexOAuthUrlProvider> {
        YandexOAuthUrlProviderImpl(
            redirectUriProvider = get(),
            tokenStorage = get(),
        )
    }
    single<YandexAuthRepository> { YandexAuthRepositoryImpl(get(), get(), get()) }


    factory { ObserveAuthStateUseCase(get()) }
    factory { ObserveAuthEventsUseCase(get()) }
    factory { RefreshAuthStateUseCase(get()) }
    factory { GetYandexUserProfileUseCase(get()) }
    factory { StartYandexOAuthCallbackServerUseCase(getOrNull() ?: NoOpYandexOAuthCallbackServer) }
    factory { StopYandexOAuthCallbackServerUseCase(getOrNull() ?: NoOpYandexOAuthCallbackServer) }
    factory { LogoutUseCase(get()) }
}

private object NoOpYandexOAuthCallbackServer : YandexOAuthCallbackServer {
    override fun start() = Unit
    override fun stop() = Unit
}