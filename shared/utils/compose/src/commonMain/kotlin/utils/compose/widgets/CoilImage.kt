package utils.compose.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.SingletonImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import utils.currentTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun CoilImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    cacheKey: String? = model?.toString(),
    memoryCacheEnabled: Boolean = true,
    diskCacheEnabled: Boolean = true,
    refreshIfStale: Duration = 5.minutes,
    placeholder: @Composable BoxScope.() -> Unit = { DefaultImagePlaceholder() },
    error: @Composable BoxScope.() -> Unit = placeholder,
) {
    val context = LocalPlatformContext.current
    var refreshVersion by remember(model, cacheKey) { mutableIntStateOf(0) }

    LaunchedEffect(model, cacheKey, refreshIfStale) {
        val staleMillis = refreshIfStale
            .takeIf { it.isPositive() }
            ?.inWholeMilliseconds
            ?: return@LaunchedEffect
        val key = cacheKey
            ?: return@LaunchedEffect
        if (model == null || !CoilImageRefreshTracker.shouldRefresh(key, staleMillis)) {
            return@LaunchedEffect
        }

        val result = try {
            SingletonImageLoader.get(context).execute(
                ImageRequest.Builder(context)
                    .data(model)
                    .apply {
                        cacheKey.let {
                            memoryCacheKey(it)
                            diskCacheKey(it)
                        }
                    }
                    .memoryCachePolicy(CachePolicy.WRITE_ONLY)
                    .diskCachePolicy(CachePolicy.WRITE_ONLY)
                    .networkCachePolicy(CachePolicy.WRITE_ONLY)
                    .build()
            )
        } catch (_: Throwable) {
            null
        } finally {
            CoilImageRefreshTracker.markRefreshed(key)
        }

        if (result is SuccessResult) {
            refreshVersion += 1
        }
    }

    val request = remember(
        model,
        cacheKey,
        memoryCacheEnabled,
        diskCacheEnabled,
        refreshVersion,
    ) {
        ImageRequest.Builder(context)
            .data(model)
            .apply {
                cacheKey?.let {
                    memoryCacheKey(it)
                    diskCacheKey(it)
                }
            }
            .memoryCachePolicy(if (memoryCacheEnabled) CachePolicy.ENABLED else CachePolicy.DISABLED)
            .diskCachePolicy(if (diskCacheEnabled) CachePolicy.ENABLED else CachePolicy.DISABLED)
            .build()
    }

    val painter = rememberAsyncImagePainter(request)
    val state by painter.state.collectAsState()

    Crossfade(state) { animatedState ->
        Box(modifier) {
            when (animatedState) {
                is AsyncImagePainter.State.Success -> Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale,
                    alignment = alignment,
                )

                is AsyncImagePainter.State.Error -> error()
                else -> placeholder()
            }
        }
    }
}

private object CoilImageRefreshTracker {
    private val refreshedAtMillisByKey = mutableMapOf<String, Long>()
    private val refreshingKeys = mutableSetOf<String>()

    fun shouldRefresh(key: String, staleMillis: Long): Boolean {
        if (!refreshingKeys.add(key)) return false

        val now = currentTimeMillis()
        val refreshedAtMillis = refreshedAtMillisByKey[key]
        val isStale = refreshedAtMillis == null || now - refreshedAtMillis >= staleMillis
        if (!isStale) {
            refreshingKeys.remove(key)
        }
        return isStale
    }

    fun markRefreshed(key: String) {
        refreshedAtMillisByKey[key] = currentTimeMillis()
        refreshingKeys.remove(key)
    }
}


@Composable
fun DefaultImagePlaceholder(
    modifier: Modifier = Modifier,
    color: Color = Color.DarkGray
) {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850),
            repeatMode = RepeatMode.Reverse,
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color.copy(alpha = alpha))
    )
}
