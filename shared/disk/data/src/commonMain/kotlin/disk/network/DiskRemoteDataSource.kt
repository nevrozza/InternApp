package disk.network

import disk.network.dto.DiskLinkResponse
import disk.network.dto.DiskResourceResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DiskRemoteDataSource(
    private val hc: HttpClient,
) {
    suspend fun getResource(path: String): DiskResourceResponse {
        return hc.get(DiskNetworkPaths.GET_RESOURCE) {
            parameter("path", path)
            parameter("fields", RESOURCE_FIELDS)
        }.body()
    }

    suspend fun listDirectory(path: String): List<DiskResourceResponse> {
        return hc.get(DiskNetworkPaths.GET_RESOURCE) {
            parameter("path", path)
            parameter("limit", DEFAULT_DIRECTORY_LIMIT)
            parameter("fields", DIRECTORY_FIELDS)
        }.body<DiskResourceResponse>().embedded?.items.orEmpty()
    }

    suspend fun createFolder(path: String) {
        hc.put(DiskNetworkPaths.GET_RESOURCE) {
            parameter("path", path)
        }
    }

    suspend fun delete(path: String) {
        hc.delete(DiskNetworkPaths.GET_RESOURCE) {
            parameter("path", path)
        }
    }

    suspend fun rename(
        sourcePath: String,
        targetPath: String,
    ) {
        hc.post(DiskNetworkPaths.MOVE_RESOURCE) {
            parameter("from", sourcePath)
            parameter("path", targetPath)
            parameter("overwrite", false)
        }
    }

    suspend fun getDownloadLink(path: String): DiskLinkResponse {
        return hc.get(DiskNetworkPaths.GET_DOWNLOAD_LINK) {
            parameter("path", path)
        }.body()
    }

    suspend fun downloadText(path: String): String {
        val link = getDownloadLink(path)
        return hc.get(link.href).bodyAsText()
    }

    suspend fun getUploadLink(
        path: String,
        overwrite: Boolean = true,
    ): DiskLinkResponse {
        return hc.get(DiskNetworkPaths.GET_UPLOAD_LINK) {
            parameter("path", path)
            parameter("overwrite", overwrite)
        }.body()
    }

    suspend fun uploadText(
        path: String,
        content: String,
        overwrite: Boolean = true,
    ) {
        val link = getUploadLink(
            path = path,
            overwrite = overwrite,
        )
        hc.put(link.href) {
            contentType(ContentType.Text.Plain)
            setBody(content)
        }
    }

    suspend fun uploadBytes(
        path: String,
        bytes: ByteArray,
        overwrite: Boolean = true,
    ) {
        val link = getUploadLink(
            path = path,
            overwrite = overwrite,
        )
        hc.put(link.href) {
            contentType(ContentType.Application.OctetStream)
            setBody(bytes)
        }
    }

    private companion object {
        const val DEFAULT_DIRECTORY_LIMIT = 100

        const val RESOURCE_FIELDS =
            "resource_id,name,path,type,modified,md5"

        const val DIRECTORY_FIELDS =
            "resource_id,name,path,type,modified,md5," +
                "_embedded.items.resource_id,_embedded.items.name,_embedded.items.path," +
                "_embedded.items.type,_embedded.items.modified,_embedded.items.md5"
    }
}
