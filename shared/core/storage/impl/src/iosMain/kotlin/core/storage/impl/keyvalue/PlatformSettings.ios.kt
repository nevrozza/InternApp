package core.storage.impl.keyvalue

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import core.common.PlatformConfig
import platform.Foundation.NSUserDefaults

internal actual fun createPlainSettings(platformConfig: PlatformConfig): Settings {
    return NSUserDefaultsSettings(
        delegate = NSUserDefaults(suiteName = PLAIN_SETTINGS_NAME)
    )
}

@OptIn(ExperimentalSettingsImplementation::class)
internal actual fun createEncryptedSettings(platformConfig: PlatformConfig): Settings {
    return KeychainSettings(
        service = ENCRYPTED_SETTINGS_NAME
    )
}