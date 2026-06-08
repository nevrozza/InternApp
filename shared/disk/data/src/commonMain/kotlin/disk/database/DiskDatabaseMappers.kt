package disk.database

import core.storage.impl.room.disk.DiskResourceEntity
import core.storage.impl.room.disk.DiskSyncOperationEntity
import disk.models.resources.BinaryFileResource
import disk.models.resources.DirectoryResource
import disk.models.resources.DiskResource
import disk.models.resources.FileResource
import disk.models.resources.TextFileResource
import disk.models.sync.SyncOperation
import disk.models.sync.SyncOperationState
import disk.models.sync.SyncOperationType
import utils.types.DiskPath
import utils.types.LocalPath
import kotlin.time.Instant

internal fun DiskResourceEntity.toDomain(): DiskResource {
    val modified = Instant.fromEpochMilliseconds(modifiedEpochMillis)
    return when (DiskResourceEntityType.valueOf(type)) {
        DiskResourceEntityType.DIRECTORY -> DirectoryResource(
            localId = localId,
            resourceId = resourceId,
            path = DiskPath(path),
            parentPath = DiskPath(parentPath),
            name = name,
            modified = modified,
        )

        DiskResourceEntityType.TEXT_FILE -> TextFileResource(
            localId = localId,
            resourceId = resourceId,
            path = DiskPath(path),
            parentPath = DiskPath(parentPath),
            name = name,
            modified = modified,
            md5 = md5,
            textContent = textContent.orEmpty(),
        )

        DiskResourceEntityType.BINARY_FILE -> BinaryFileResource(
            localId = localId,
            resourceId = resourceId,
            path = DiskPath(path),
            parentPath = DiskPath(parentPath),
            name = name,
            modified = modified,
            md5 = md5,
            localFilePath = localFilePath?.let(::LocalPath),
        )
    }
}

internal fun DiskResource.toEntity(): DiskResourceEntity {
    return DiskResourceEntity(
        localId = localId,
        resourceId = resourceId,
        path = path.value,
        parentPath = parentPath.value,
        name = name,
        type = toEntityType().name,
        md5 = (this as? FileResource)?.md5,
        modifiedEpochMillis = modified.toEpochMilliseconds(),
        textContent = (this as? TextFileResource)?.textContent,
        localFilePath = (this as? BinaryFileResource)?.localFilePath?.value,
    )
}

private fun DiskResource.toEntityType(): DiskResourceEntityType {
    return when (this) {
        is DirectoryResource -> DiskResourceEntityType.DIRECTORY
        is TextFileResource -> DiskResourceEntityType.TEXT_FILE
        is BinaryFileResource -> DiskResourceEntityType.BINARY_FILE
    }
}

internal fun DiskSyncOperationEntity.toDomain(): SyncOperation {
    return SyncOperation(
        id = id,
        resourceLocalId = resourceLocalId,
        resourceId = resourceId,
        operation = SyncOperationType.valueOf(operation),
        path = DiskPath(path),
        targetPath = targetPath?.let(::DiskPath),
        state = SyncOperationState.valueOf(state),
        lastError = lastError,
    )
}

internal fun SyncOperation.toEntity(): DiskSyncOperationEntity {
    return DiskSyncOperationEntity(
        id = id,
        resourceLocalId = resourceLocalId,
        resourceId = resourceId,
        operation = operation.name,
        path = path.value,
        targetPath = targetPath?.value,
        state = state.name,
        lastError = lastError,
    )
}
