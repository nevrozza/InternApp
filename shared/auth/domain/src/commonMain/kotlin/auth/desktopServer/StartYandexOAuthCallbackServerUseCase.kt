package auth.desktopServer

class StartYandexOAuthCallbackServerUseCase(
    private val server: YandexOAuthCallbackServer,
) {
    operator fun invoke() {
        server.start()
    }
}
