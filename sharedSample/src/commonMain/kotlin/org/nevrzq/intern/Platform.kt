package org.nevrzq.intern

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform