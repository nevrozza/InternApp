package core.network.ktor

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual val HttpClientEngineFactory: HttpClientEngineFactory<HttpClientEngineConfig> = Darwin