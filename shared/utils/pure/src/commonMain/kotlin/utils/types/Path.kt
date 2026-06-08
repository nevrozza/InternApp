package utils.types

import kotlin.jvm.JvmInline

fun String.isTextFileName(): Boolean {
    return endsWith(".txt", ignoreCase = true)
}

@JvmInline
value class LocalPath(val value: String)

@JvmInline
value class DiskPath(val value: String) {
    fun child(name: String): DiskPath {
        val parent = value.trimEnd('/')
        return DiskPath("$parent/$name")
    }

    fun withName(name: String): DiskPath {
        return parent().child(name)
    }

    fun parent(): DiskPath {
        val raw = value.removePrefix("disk:")
        val parent = raw.substringBeforeLast("/", missingDelimiterValue = "/")
        return DiskPath("disk:$parent")
    }

    fun name(): String {
        return value.substringAfterLast("/")
    }
}