package core.storage.impl.keyvalue

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import core.common.PlatformConfig
import java.util.prefs.Preferences

internal actual fun createPlainSettings(platformConfig: PlatformConfig): Settings {
    return PreferencesSettings(
        delegate = Preferences.userRoot().node("preferences")
    )
}

internal actual fun createEncryptedSettings(platformConfig: PlatformConfig): Settings {
    // TODO: заплакать от того, что на JVM это мегахард сделать

    return PreferencesSettings(
        delegate = Preferences.userRoot().node("encrypted_preferences")
    )
}