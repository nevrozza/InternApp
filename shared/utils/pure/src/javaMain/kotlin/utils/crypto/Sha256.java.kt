package utils.crypto

import java.security.MessageDigest

actual object Sha256 {
    actual fun digest(data: ByteArray): ByteArray {
        return MessageDigest
            .getInstance("SHA-256")
            .digest(data)
    }
}