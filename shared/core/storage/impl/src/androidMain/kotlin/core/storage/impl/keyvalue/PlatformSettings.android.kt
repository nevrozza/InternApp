package core.storage.impl.keyvalue

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import core.common.PlatformConfig

internal actual fun createPlainSettings(
    platformConfig: PlatformConfig
): Settings {
    val sharedPreferences = platformConfig.context.getSharedPreferences(
        PLAIN_SETTINGS_NAME,
        Context.MODE_PRIVATE
    )

    return SharedPreferencesSettings(sharedPreferences)
}


@Suppress("deprecation")
internal actual fun createEncryptedSettings(platformConfig: PlatformConfig): Settings {
    // TODO: Replace deprecated EncryptedSharedPreferences

    val context = platformConfig.context

    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        ENCRYPTED_SETTINGS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    return SharedPreferencesSettings(encryptedSharedPreferences)
}
