package core.network

import core.network.ktor.getHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

val coreNetworkModule = module {
    single<HttpClient> {
        getHttpClient(
            CIO
        )
    }
}