package dev.alinborcea.kcloud

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform