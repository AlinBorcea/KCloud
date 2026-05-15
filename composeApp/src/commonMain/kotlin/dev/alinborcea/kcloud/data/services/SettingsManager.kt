package dev.alinborcea.kcloud.data.services

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.encodeValue
import dev.alinborcea.kcloud.domain.models.UserSettings
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
class SettingsManager(private val settings: Settings = Settings()) {

    private val SETTINGS_KEY = "user_settings_v1"

    @OptIn(ExperimentalSettingsApi::class)
    fun saveSettings(userSettings: UserSettings) {
        settings.encodeValue(UserSettings.serializer(), SETTINGS_KEY, userSettings)
    }

    @OptIn(ExperimentalSettingsApi::class)
    fun loadSettings(): UserSettings {
        return try {
            settings.decodeValue(UserSettings.serializer(), SETTINGS_KEY, UserSettings())
        } catch (e: Exception) {
            e.printStackTrace()
            UserSettings()
        }
    }
}