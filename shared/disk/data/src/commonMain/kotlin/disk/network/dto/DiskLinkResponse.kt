package disk.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiskLinkResponse(
    @SerialName("href") val href: String,
    @SerialName("method") val method: String? = null,
    @SerialName("templated") val templated: Boolean? = null,
)
