package core.storage.impl.keyvalue

import com.russhwolf.settings.Settings


internal const val PLAIN_SETTINGS_NAME = "org.nevrzq.intern.plain_settings"
internal const val ENCRYPTED_SETTINGS_NAME = "org.nevrzq.intern.encrypted_settings"

internal expect class PlatformConfig

internal expect fun createPlainSettings(platformConfig: PlatformConfig): Settings

internal expect fun createEncryptedSettings(platformConfig: PlatformConfig): Settings