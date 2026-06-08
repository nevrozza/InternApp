package core.storage.impl.room.disk

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "disk_sync_out",
    indices = [
        Index(value = ["resourceLocalId"]),
        Index(value = ["resourceId"]),
        Index(value = ["path"]),
        Index(value = ["state"]),
    ],
)
data class DiskSyncOperationEntity(
    @PrimaryKey val id: String,
    val resourceLocalId: String?,
    val resourceId: String?,
    val operation: String,
    val path: String,
    val targetPath: String?,
    val state: String,
)
