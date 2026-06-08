package core.storage.impl.room.disk

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DiskResourceDao {

    @Query(
        """
        SELECT * FROM disk_resources
        WHERE parentPath = :parentPath
        ORDER BY
            CASE WHEN type = 'DIRECTORY' THEN 0 ELSE 1 END,
            name COLLATE NOCASE ASC
        """,
    )
    fun observeParentPathContent(parentPath: String): Flow<List<DiskResourceEntity>>

    @Query(
        """
        SELECT * FROM disk_resources
        WHERE parentPath = :parentPath
        ORDER BY
            CASE WHEN type = 'DIRECTORY' THEN 0 ELSE 1 END,
            name COLLATE NOCASE ASC
        """,
    )
    suspend fun getParentPathContent(parentPath: String): List<DiskResourceEntity>

    @Query("SELECT * FROM disk_resources WHERE path = :path LIMIT 1")
    suspend fun getByPath(path: String): DiskResourceEntity?

    @Query("SELECT * FROM disk_resources WHERE localId = :localId LIMIT 1")
    suspend fun getByLocalId(localId: String): DiskResourceEntity?

    @Upsert
    suspend fun upsert(resource: DiskResourceEntity)

    @Upsert
    suspend fun upsert(resources: List<DiskResourceEntity>)

    @Query("DELETE FROM disk_resources WHERE path = :path")
    suspend fun deleteByPath(path: String)

    @Query("DELETE FROM disk_resources WHERE localId = :localId")
    suspend fun deleteByLocalId(localId: String)
}
