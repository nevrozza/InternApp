package org.nevrzq.intern.auth

import auth.repositories.YandexAuthRepository
import auth.desktopServer.YandexOAuthCallbackServer
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class KtorYandexOAuthCallbackServer(
    private val yandexAuthRepository: YandexAuthRepository,
) : YandexOAuthCallbackServer {

    private var server: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? =
        null

    override fun start() {
        if (server != null) return

        server = embeddedServer(
            factory = CIO,
            host = "localhost",
            port = 8042,
        ) {
            routing {
                get("/oauth/yandex/callback") {
                    val response = yandexAuthRepository.handleOAuthCallback(
                        parameters = call.request.queryParameters.entries()
                            .associate { entry -> entry.key to entry.value.first() },
                    )

                    call.respondText(
                        text = response,
                        contentType = ContentType.Text.Plain,
                        status = HttpStatusCode.OK,
                    )
                }
            }
        }.start(wait = false)
    }

    override fun stop() {
        server?.stop()
        server = null
    }
}
