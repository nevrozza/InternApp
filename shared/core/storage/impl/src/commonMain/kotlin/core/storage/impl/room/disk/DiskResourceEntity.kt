package core.storage.impl.room.disk

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "disk_resources",
    indices = [
        Index(value = ["resourceId"], unique = true),
        Index(value = ["path"], unique = true),
        Index(value = ["parentPath"]),
    ],
)
data class DiskResourceEntity(
    @PrimaryKey val localId: String,
    val resourceId: String?,
    val path: String,
    val parentPath: String,
    val name: String,
    val type: String,
    val md5: String?,
    val modifiedEpochMillis: Long,
    val textContent: String?,
    val localFilePath: String?,
)
