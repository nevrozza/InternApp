package auth.desktopServer

class StopYandexOAuthCallbackServerUseCase(
    private val server: YandexOAuthCallbackServer,
) {
    operator fun invoke() {
        server.stop()
    }
}
