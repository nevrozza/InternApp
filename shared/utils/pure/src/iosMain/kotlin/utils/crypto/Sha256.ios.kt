package utils.crypto

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_SHA256

actual object Sha256 {
    @OptIn(ExperimentalForeignApi::class)
    actual fun digest(data: ByteArray): ByteArray {
        // AI Generated
        val result = ByteArray(32)

        data.usePinned { dataPinned ->
            result.usePinned { resultPinned ->
                CC_SHA256(
                    dataPinned.addressOf(0),
                    data.size.toUInt(),
                    resultPinned.addressOf(0).reinterpret()
                )
            }
        }

        return result
    }
}