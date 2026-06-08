package core.storage.impl.database.disk

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DiskSyncOperationDao {

    @Query("SELECT * FROM disk_sync_out ORDER BY id ASC")
    fun observeAll(): Flow<List<DiskSyncOperationEntity>>

    @Query(
        """
        SELECT * FROM disk_sync_out
        WHERE state = :state
        ORDER BY id ASC
        """,
    )
    suspend fun getByState(state: String): List<DiskSyncOperationEntity>

    @Query(
        """
        SELECT * FROM disk_sync_out
        WHERE resourceLocalId = :resourceLocalId
        AND state IN (:states)
        LIMIT 1
        """,
    )
    suspend fun getActiveByResourceLocalId(
        resourceLocalId: String,
        states: List<String>,
    ): DiskSyncOperationEntity?

    @Upsert
    suspend fun upsert(operation: DiskSyncOperationEntity)

    @Query("UPDATE disk_sync_out SET state = :state WHERE id = :id")
    suspend fun updateState(id: String, state: String)

    @Query("DELETE FROM disk_sync_out WHERE id = :id")
    suspend fun deleteById(id: String)
}
