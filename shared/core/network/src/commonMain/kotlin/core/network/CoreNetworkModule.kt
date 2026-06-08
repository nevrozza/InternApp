package core.network

import core.network.ktor.getHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreNetworkModule = module {
    single<HttpClient> {
        getHttpClient(
            CIO,
            diPlugins = getAll()
        )
    }

    single<HttpClient>(named("auth")) {
        getHttpClient(
            CIO,
            diPlugins = listOf()
        )
    }

}