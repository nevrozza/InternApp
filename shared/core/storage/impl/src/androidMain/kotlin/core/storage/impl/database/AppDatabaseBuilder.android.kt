package core.storage.impl.database

import androidx.room.Room
import androidx.room.RoomDatabase
import core.common.PlatformConfig

internal actual fun getAppDatabaseBuilder(
    platformConfig: PlatformConfig,
): RoomDatabase.Builder<AppDatabase> {
    return Room.databaseBuilder<AppDatabase>(
        context = platformConfig.context,
        name = DATABASE_NAME,
    )
}
