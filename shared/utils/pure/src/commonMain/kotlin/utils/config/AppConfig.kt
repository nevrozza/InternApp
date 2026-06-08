package utils.config


object AppConfig {
    object YandexOAuthConfig {
        const val clientId: String = "794eb4dfcc3c455d908f75a98a927367"

        object Desktop {
            private const val redirectUri: String = "http://localhost:8042/oauth/yandex/callback"
            private const val encodedRedirectUri: String =
                "http%3A%2F%2Flocalhost%3A8042%2Foauth%2Fyandex%2Fcallback"
            const val authUrl: String =
                "https://oauth.yandex.ru/authorize?response_type=code&client_id=$clientId&redirect_uri=$encodedRedirectUri"
        }

        object Android {
            private const val redirectUri: String = "org.nevrzq.intern://oauth/yandex/callback"
            private const val encodedRedirectUri: String =
                "org.nevrzq.intern%3A%2F%2Foauth%2Fyandex%2Fcallback"
            const val authUrl: String =
                "https://oauth.yandex.ru/authorize?response_type=code&client_id=$clientId&redirect_uri=$encodedRedirectUri"
        }

        object Ios {
            // TODO
            const val redirectUri: String = ""
            const val authUrl: String = ""
        }
    }
}
