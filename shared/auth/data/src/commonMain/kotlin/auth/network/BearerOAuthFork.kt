package auth.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.utils.io.KtorDsl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.concurrent.Volatile
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

fun AuthConfig.oauth(block: OAuthAuthConfig.() -> Unit) {
    val config = OAuthAuthConfig().apply(block)

    this.providers.add(
        OAuthAuthProvider(
            refreshTokens = config.refreshTokens,
            loadTokens = config.loadTokens,
            sendWithoutRequestCallback = config.sendWithoutRequest,
            realm = config.realm,
            cacheTokens = config.cacheTokens,
            nonCancellableRefresh = config.nonCancellableRefresh
        )
    )
}

data class OAuthTokens(
    val accessToken: String,
    val refreshToken: String?
)

class RefreshOAuthTokensParams(
    val client: HttpClient,
    val response: HttpResponse,
    val oldTokens: OAuthTokens?
) {
    fun HttpRequestBuilder.markAsRefreshTokenRequest() {
        attributes.put(AuthCircuitBreaker, Unit)
    }
}

@KtorDsl
class OAuthAuthConfig {
    internal var refreshTokens: suspend RefreshOAuthTokensParams.() -> OAuthTokens? = { null }
    internal var loadTokens: suspend () -> OAuthTokens? = { null }
    internal var sendWithoutRequest: (HttpRequestBuilder) -> Boolean = { true }

    var realm: String? = null
    var cacheTokens: Boolean = true
    var nonCancellableRefresh: Boolean = false

    fun refreshTokens(block: suspend RefreshOAuthTokensParams.() -> OAuthTokens?) {
        refreshTokens = block
    }

    fun loadTokens(block: suspend () -> OAuthTokens?) {
        loadTokens = block
    }

    fun sendWithoutRequest(block: (HttpRequestBuilder) -> Boolean) {
        sendWithoutRequest = block
    }
}

class OAuthAuthProvider(
    private val refreshTokens: suspend RefreshOAuthTokensParams.() -> OAuthTokens?,
    loadTokens: suspend () -> OAuthTokens?,
    private val sendWithoutRequestCallback: (HttpRequestBuilder) -> Boolean = { true },
    private val realm: String?,
    cacheTokens: Boolean = true,
    private val nonCancellableRefresh: Boolean = false,
) : AuthProvider {

    @Suppress("OverridingDeprecatedMember")
    @Deprecated(
        message = "Please use sendWithoutRequest function instead",
        level = DeprecationLevel.ERROR
    )
    override val sendWithoutRequest: Boolean
        get() = error("Deprecated")

    private val tokensHolder = AuthTokenHolder(loadTokens, cacheTokens)

    override fun sendWithoutRequest(request: HttpRequestBuilder): Boolean {
        return sendWithoutRequestCallback(request)
    }

    override fun isApplicable(auth: HttpAuthHeader): Boolean {
        if (!auth.authScheme.equals("OAuth", ignoreCase = true)) {
            return false
        }

        return when {
            realm == null -> true
            auth !is HttpAuthHeader.Parameterized -> false
            else -> auth.parameter("realm") == realm
        }
    }

    override suspend fun addRequestHeaders(
        request: HttpRequestBuilder,
        authHeader: HttpAuthHeader?
    ) {
        val token = tokensHolder.loadToken() ?: return

        request.headers {
            remove(HttpHeaders.Authorization)

            if (!request.attributes.contains(AuthCircuitBreaker)) {
                append(
                    HttpHeaders.Authorization,
                    "OAuth ${token.accessToken}"
                )
            }
        }
    }

    override suspend fun refreshToken(response: HttpResponse): Boolean {
        val newToken = tokensHolder.setToken(nonCancellableRefresh) {
            refreshTokens(
                RefreshOAuthTokensParams(
                    client = response.call.client,
                    response = response,
                    oldTokens = tokensHolder.loadToken()
                )
            )
        }

        return newToken != null
    }

    override fun clearToken() {
        tokensHolder.clearToken()
    }
}

internal class AuthTokenHolder<T>(
    private val loadTokens: suspend () -> T?,
    private val cacheTokens: Boolean = true
) {
    @Volatile
    private var value: T? = null

    @Volatile
    private var isLoadRequest = false

    private val mutex = Mutex()

    internal suspend fun loadToken(): T? {
        if (!cacheTokens) {
            return loadTokens()
        }

        if (value != null) return value

        val prevValue = value

        return if (coroutineContext[SetTokenContext] != null) {
            value = loadTokens()
            value
        } else {
            mutex.withLock {
                isLoadRequest = true

                try {
                    if (prevValue == value) {
                        value = loadTokens()
                    }
                } finally {
                    isLoadRequest = false
                }

                value
            }
        }
    }

    private class SetTokenContext : CoroutineContext.Element {
        override val key: CoroutineContext.Key<*>
            get() = SetTokenContext

        companion object : CoroutineContext.Key<SetTokenContext>
    }

    private val setTokenMarker = SetTokenContext()

    internal suspend fun setToken(
        nonCancellable: Boolean = false,
        block: suspend () -> T?
    ): T? {
        val prevValue = value
        val lockedByLoad = isLoadRequest

        val context = if (nonCancellable) {
            coroutineContext + NonCancellable + setTokenMarker
        } else {
            coroutineContext + setTokenMarker
        }

        return withContext(context) {
            mutex.withLock {
                if (prevValue == value || lockedByLoad) {
                    value = block()
                }

                value
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    internal fun clearToken(
        coroutineScope: CoroutineScope = GlobalScope
    ) {
        if (mutex.tryLock()) {
            value = null
            mutex.unlock()
        } else {
            coroutineScope.launch {
                mutex.withLock {
                    value = null
                }
            }
        }
    }
}