package utils.config


object AppConfig {
    object YandexOAuthConfig {
        const val clientId: String = "794eb4dfcc3c455d908f75a98a927367"

        object Desktop {
            private const val redirectUri: String = "http://localhost:8042/oauth/yandex/callback"
            const val authUrl: String =
                "https://oauth.yandex.ru/authorize?response_type=code&client_id=$clientId&redirect_uri=$redirectUri"
        }

        object Mobile {
            private const val redirectUri: String = "org.nevrzq.intern://oauth/yandex/callback"
            const val authUrl: String =
                "https://oauth.yandex.ru/authorize?response_type=code&client_id=$clientId&redirect_uri=$redirectUri"
        }
    }
}
