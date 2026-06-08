package core.network.api

import io.ktor.client.HttpClientConfig


interface KtorClientPlugin {
    fun install(config: HttpClientConfig<*>)
}