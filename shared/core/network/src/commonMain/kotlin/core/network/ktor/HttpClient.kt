package core.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun getHttpClient(
    engineFactory: HttpClientEngineFactory<HttpClientEngineConfig>
) =
    HttpClient(engineFactory) {
        install(Logging) {
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            connectTimeoutMillis = 15000
            requestTimeoutMillis = 30000
        }

        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(DefaultRequest) {
            this.contentType(ContentType.Application.Json)
        }

        expectSuccess = true
    }