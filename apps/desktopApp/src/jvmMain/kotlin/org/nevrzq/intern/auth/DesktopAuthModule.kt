package org.nevrzq.intern.auth

import auth.desktopServer.YandexOAuthCallbackServer
import auth.repositories.YandexOAuthUrlProvider
import org.koin.dsl.module
import utils.config.AppConfig

val desktopAuthCallbackServerModule = module {
    single<YandexOAuthCallbackServer> {
        KtorYandexOAuthCallbackServer(
            yandexAuthRepository = get(),
        )
    }

    single<YandexOAuthUrlProvider> {
        YandexOAuthUrlProvider {
            AppConfig.YandexOAuthConfig.Desktop.authUrl
        }
    }
}
