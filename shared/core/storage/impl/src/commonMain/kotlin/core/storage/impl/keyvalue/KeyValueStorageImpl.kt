package core.storage.impl.keyvalue

import com.russhwolf.settings.Settings
import core.storage.keyvalue.EncryptedKeyValueStorage
import core.storage.keyvalue.KeyValueStorage
import core.storage.keyvalue.PlainKeyValueStorage

internal class EncryptedKeyValueStorageImpl(
    settings: Settings
) : KeyValueStorage by KeyValueStorageImpl(settings), EncryptedKeyValueStorage

internal class PlainKeyValueStorageImpl(
    settings: Settings
) : KeyValueStorage by KeyValueStorageImpl(settings), PlainKeyValueStorage

private class KeyValueStorageImpl(
    private val settings: Settings
) : KeyValueStorage {
    override fun get(key: String): String? = settings.getStringOrNull(key)

    override fun get(key: String, default: String): String = settings.getString(key, default)

    override fun set(key: String, value: String) = settings.putString(key, value)

    override fun remove(key: String) = settings.remove(key)

    override fun clear() = settings.clear()
}