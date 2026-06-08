package utils.crypto

expect object Sha256 {
    fun digest(data: ByteArray): ByteArray
}