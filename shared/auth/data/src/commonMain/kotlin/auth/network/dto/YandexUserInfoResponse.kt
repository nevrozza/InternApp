package auth.network.dto

import auth.models.YandexUserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YandexUserInfoResponse(
    @SerialName("display_name")
    val displayName: String? = null,

    @SerialName("is_avatar_empty")
    val isAvatarEmpty: Boolean? = null,

    @SerialName("default_avatar_id")
    val defaultAvatarId: String? = null,
)

internal fun YandexUserInfoResponse.toDomain(): YandexUserProfile =
    YandexUserProfile(
        displayName = displayName.toString(),
        avatarUrl = defaultAvatarId
            ?.takeUnless { isAvatarEmpty == true }
            ?.let { avatarId -> "https://avatars.yandex.net/get-yapic/$avatarId/islands-200" },
    )