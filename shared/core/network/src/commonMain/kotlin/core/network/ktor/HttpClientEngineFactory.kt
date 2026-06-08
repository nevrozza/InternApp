package core.network.ktor


import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

expect val HttpClientEngineFactory: HttpClientEngineFactory<HttpClientEngineConfig>