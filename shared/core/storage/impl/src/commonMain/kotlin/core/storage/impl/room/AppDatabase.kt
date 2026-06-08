package core.storage.impl.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import core.storage.impl.room.disk.DiskResourceDao
import core.storage.impl.room.disk.DiskResourceEntity
import core.storage.impl.room.disk.DiskSyncOperationDao
import core.storage.impl.room.disk.DiskSyncOperationEntity

@Database(
    entities = [
        DiskResourceEntity::class,
        DiskSyncOperationEntity::class,
    ],
    version = 1,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diskResourceDao(): DiskResourceDao

    abstract fun diskSyncOperationDao(): DiskSyncOperationDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
