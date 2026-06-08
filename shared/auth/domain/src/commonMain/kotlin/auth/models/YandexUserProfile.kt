package auth.models

import androidx.compose.runtime.Immutable


@Immutable
data class YandexUserProfile(
    val displayName: String,
    val avatarUrl: String?
)
