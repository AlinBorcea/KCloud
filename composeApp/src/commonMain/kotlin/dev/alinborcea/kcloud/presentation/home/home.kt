package dev.alinborcea.kcloud.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.alinborcea.kcloud.data.repositories.WeatherAPI
import dev.alinborcea.kcloud.domain.models.WeatherResponse
import kotlinx.coroutines.runBlocking

@Composable
fun HomePage() {
    val weather = WeatherAPI()

    var weatherInfo by remember { mutableStateOf(WeatherResponse()) }
    var forecast by remember { mutableStateOf(WeatherResponse()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        WeatherSearchBar(
            query = "",
            onSearch = { city ->
                runBlocking {
                    try {
                        weatherInfo = weather.getWeatherAt(city)
                        forecast = weather.getWeatherForecast(city, 4)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                println("Searching for: $city")
            }
        )

        WeatherSummaryCard(data = weatherInfo)
        ForecastSection(forecast.forecast.forecastDay)
    }
}