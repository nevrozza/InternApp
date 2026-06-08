package disk.models.sync

import disk.models.DiskPath


data class SyncOperation(
    val id: String,
    val resourceLocalId: String?,
    val resourceId: String?,
    val operation: SyncOperationType,
    val path: DiskPath,
    val targetPath: DiskPath?,
    val state: SyncOperationState,
    val lastError: String?,
)
