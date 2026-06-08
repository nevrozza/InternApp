package org.nevrzq.intern.auth

import auth.desktopServer.YandexOAuthCallbackServer
import auth.repositories.YandexOAuthRedirectUriProvider
import org.koin.dsl.module
import utils.config.AppConfig

val desktopAuthCallbackServerModule = module {
    single<YandexOAuthCallbackServer> {
        KtorYandexOAuthCallbackServer(
            yandexAuthRepository = get(),
        )
    }

    single<YandexOAuthRedirectUriProvider> {
        YandexOAuthRedirectUriProvider {
            AppConfig.YandexOAuthConfig.Desktop.redirectUri
        }
    }
}
