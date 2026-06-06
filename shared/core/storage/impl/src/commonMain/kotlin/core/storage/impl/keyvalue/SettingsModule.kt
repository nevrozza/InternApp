package core.storage.impl.keyvalue

import com.russhwolf.settings.Settings
import core.storage.keyvalue.EncryptedKeyValueStorage
import core.storage.keyvalue.PlainKeyValueStorage
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val PLAIN_SETTINGS = "plain_settings"
private const val ENCRYPTED_SETTINGS = "encrypted_settings"

internal val settingsModule = module {
    single<Settings>(named(PLAIN_SETTINGS)) {
        createPlainSettings(get())
    }

    single<Settings>(named(ENCRYPTED_SETTINGS)) {
        createEncryptedSettings(get())
    }

    single<PlainKeyValueStorage> {
        PlainKeyValueStorageImpl(
            settings = get(named(PLAIN_SETTINGS))
        )
    }

    single<EncryptedKeyValueStorage> {
        EncryptedKeyValueStorageImpl(
            settings = get(named(ENCRYPTED_SETTINGS))
        )
    }
}