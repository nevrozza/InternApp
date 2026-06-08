package core.network

import core.network.ktor.HttpClientEngineFactory
import core.network.ktor.getHttpClient
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreNetworkModule = module {
    single<HttpClient> {
        getHttpClient(
            HttpClientEngineFactory,
            diPlugins = getAll()
        )
    }

    single<HttpClient>(named("auth")) {
        getHttpClient(
            HttpClientEngineFactory,
            diPlugins = listOf()
        )
    }

}