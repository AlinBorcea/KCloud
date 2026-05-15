package dev.alinborcea.kcloud.presentation.home

import dev.alinborcea.kcloud.data.repositories.WeatherAPI
import dev.alinborcea.kcloud.data.services.SettingsManager
import dev.alinborcea.kcloud.domain.models.UserSettings
import dev.alinborcea.kcloud.domain.models.WeatherResponse
import kotlinx.coroutines.runBlocking

class HomeViewModel(val weatherAPI: WeatherAPI, val settingsManager: SettingsManager) {

    fun loadSettings(): UserSettings = settingsManager.loadSettings()

    fun saveSettings(userSettings: UserSettings) = settingsManager.saveSettings(userSettings)

    fun updatedCurrentWeatherResponse(location: String): WeatherResponse {
        var weatherResponse = WeatherResponse()
        runBlocking {
            try {
                weatherResponse = weatherAPI.getWeatherAt(location)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return weatherResponse
    }

    fun updatedWeatherForecastResponse(location: String): WeatherResponse {
        var weatherResponse = WeatherResponse()
        runBlocking {
            try {
                weatherResponse = weatherAPI.getWeatherForecast(location, 3)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return weatherResponse
    }
}