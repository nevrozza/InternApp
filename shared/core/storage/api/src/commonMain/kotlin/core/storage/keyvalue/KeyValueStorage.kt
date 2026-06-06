package core.storage.keyvalue


interface EncryptedKeyValueStorage : KeyValueStorage
interface PlainKeyValueStorage : KeyValueStorage

interface KeyValueStorage {
    operator fun get(key: String): String?

    operator fun get(key: String, default: String): String

    operator fun set(key: String, value: String)

    fun remove(key: String)

    fun clear()
}