package disk.network

import disk.models.resources.BinaryFileResource
import disk.models.resources.DirectoryResource
import disk.models.resources.DiskResource
import disk.models.resources.TextFileResource
import disk.network.dto.DiskResourceResponse
import utils.types.DiskPath
import utils.types.isTextFileName
import kotlin.time.Instant

private const val REMOTE_TYPE_DIRECTORY = "dir"

internal fun DiskResourceResponse.toDomain(
    localId: String? = null,
    textContent: String? = null,
): DiskResource {
    val diskPath = DiskPath(path)
    val modifiedInstant = Instant.parse(modified)
    val resolvedLocalId = localId ?: resourceId ?: path

    return if (type == REMOTE_TYPE_DIRECTORY) {
        DirectoryResource(
            localId = resolvedLocalId,
            resourceId = resourceId,
            path = diskPath,
            parentPath = diskPath.parent(),
            name = name,
            modified = modifiedInstant,
        )
    } else if (name.isTextFileName()) {
        TextFileResource(
            localId = resolvedLocalId,
            resourceId = resourceId,
            path = diskPath,
            parentPath = diskPath.parent(),
            name = name,
            modified = modifiedInstant,
            md5 = md5,
            textContent = textContent.orEmpty(),
        )
    } else {
        BinaryFileResource(
            localId = resolvedLocalId,
            resourceId = resourceId,
            path = diskPath,
            parentPath = diskPath.parent(),
            name = name,
            modified = modifiedInstant,
            md5 = md5,
            localFilePath = null,
        )
    }
}
