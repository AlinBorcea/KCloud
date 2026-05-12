package dev.alinborcea.kcloud.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val favoriteLocation: String = "",
    val useMetric: Boolean = true
)
