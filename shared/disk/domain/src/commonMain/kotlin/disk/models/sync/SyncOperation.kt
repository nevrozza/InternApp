package disk.models.sync

import androidx.compose.runtime.Immutable
import utils.types.DiskPath

@Immutable
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
