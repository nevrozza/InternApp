package utils.config


object AppConfig {
    object YandexOAuthConfig {
        const val clientId: String = "794eb4dfcc3c455d908f75a98a927367"
        const val scope: String =
            "login:info login:avatar cloud_api:disk.read cloud_api:disk.write cloud_api:disk.info"

        object Desktop {
            const val redirectUri: String = "http://localhost:8042/oauth/yandex/callback"
        }

        object Mobile {
            const val redirectUri: String = "org.nevrzq.intern://oauth/yandex/callback"
        }
    }
}
