package core.storage.impl.room

import androidx.room.Room
import androidx.room.RoomDatabase
import core.common.PlatformConfig
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
internal actual fun getAppDatabaseBuilder(
    platformConfig: PlatformConfig,
): RoomDatabase.Builder<AppDatabase> {
    val documentsDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )?.path.orEmpty()

    return Room.databaseBuilder<AppDatabase>(
        name = "$documentsDirectory/${DATABASE_NAME}",
    )
}
