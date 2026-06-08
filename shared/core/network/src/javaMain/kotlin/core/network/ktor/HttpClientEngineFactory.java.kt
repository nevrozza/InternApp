package core.network.ktor

import io.ktor.client.engine.okhttp.OkHttp

actual val HttpClientEngineFactory: io.ktor.client.engine.HttpClientEngineFactory<io.ktor.client.engine.HttpClientEngineConfig> =
    OkHttp