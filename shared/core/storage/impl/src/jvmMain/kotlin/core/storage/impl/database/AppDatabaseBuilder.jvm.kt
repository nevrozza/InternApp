package core.storage.impl.database

import androidx.room.Room
import androidx.room.RoomDatabase
import core.common.PlatformConfig
import java.io.File

internal actual fun getAppDatabaseBuilder(
    platformConfig: PlatformConfig,
): RoomDatabase.Builder<AppDatabase> {
    val databaseFile = File(
        System.getProperty("user.home"),
        ".intern-app/$DATABASE_NAME",
    )
    databaseFile.parentFile?.mkdirs()

    return Room.databaseBuilder<AppDatabase>(
        name = databaseFile.absolutePath,
    )
}
