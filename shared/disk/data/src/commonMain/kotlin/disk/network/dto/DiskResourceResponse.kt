package disk.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiskResourceResponse(
    @SerialName("resource_id") val resourceId: String? = null,
    @SerialName("name") val name: String,
    @SerialName("path") val path: String,
    @SerialName("type") val type: String,
    @SerialName("modified") val modified: String,
    @SerialName("md5") val md5: String? = null,
    @SerialName("_embedded") val embedded: DiskEmbeddedResourcesResponse? = null,
)


@Serializable
data class DiskEmbeddedResourcesResponse(
    @SerialName("items") val items: List<DiskResourceResponse> = emptyList(),
)
