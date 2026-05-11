package dev.alinborcea.kcloud.data.repositories

import dev.alinborcea.kcloud.domain.models.WeatherResponse

interface WeatherRepository {
    suspend fun getWeatherAt(location: String): WeatherResponse
}