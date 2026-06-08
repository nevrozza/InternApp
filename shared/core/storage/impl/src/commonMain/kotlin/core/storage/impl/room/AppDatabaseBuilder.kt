package core.storage.impl.room

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import core.common.PlatformConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal const val DATABASE_NAME = "intern-app.db"

internal expect fun getAppDatabaseBuilder(
    platformConfig: PlatformConfig,
): RoomDatabase.Builder<AppDatabase>

internal fun createAppDatabase(
    builder: RoomDatabase.Builder<AppDatabase>,
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
