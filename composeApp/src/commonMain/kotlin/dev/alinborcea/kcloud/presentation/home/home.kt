package dev.alinborcea.kcloud.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.alinborcea.kcloud.data.repositories.WeatherAPI
import dev.alinborcea.kcloud.data.services.SettingsManager
import dev.alinborcea.kcloud.domain.models.WeatherResponse
import kotlinx.coroutines.runBlocking

@Composable
fun HomePage(weather: WeatherAPI, settingsManager: SettingsManager) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Forecast", "Settings")
    val icons = listOf(Icons.Default.Home, Icons.Default.Cloud, Icons.Default.Settings)

    var userSettings by remember { mutableStateOf(settingsManager.loadSettings()) }
    var weatherInfo by remember {
        mutableStateOf(
            updatedCurrentWeatherResponse(
                weather,
                userSettings.favoriteLocation
            )
        )
    }
    var forecast by remember {
        mutableStateOf(
            updatedWeatherForecastResponse(
                weather,
                userSettings.favoriteLocation
            )
        )
    }

    Scaffold(bottomBar = {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(icons[index], contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            if (selectedItem == 0) {
                WeatherSearchBar(
                    query = userSettings.favoriteLocation,
                    onSearch = { city ->
                        weatherInfo = updatedCurrentWeatherResponse(weather, city)
                        forecast = updatedWeatherForecastResponse(weather, city)
                    }
                )

                WeatherSummaryCard(data = weatherInfo)
                ForecastSection(forecast.forecast.forecastDay)

            } else if (selectedItem == 2) {
                SettingsScreen(
                    settings = userSettings,
                    onSettingsChanged = { updated ->
                        userSettings = updated
                        settingsManager.saveSettings(updated)
                        weatherInfo =
                            updatedCurrentWeatherResponse(weather, userSettings.favoriteLocation)
                        forecast =
                            updatedWeatherForecastResponse(weather, userSettings.favoriteLocation)
                    }
                )
            }
        }
    }
}

fun updatedCurrentWeatherResponse(weatherAPI: WeatherAPI, location: String): WeatherResponse {
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

fun updatedWeatherForecastResponse(weatherAPI: WeatherAPI, location: String): WeatherResponse {
    var weatherResponse = WeatherResponse()
    runBlocking {
        try {
            weatherResponse = weatherAPI.getWeatherForecast(location, 4)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return weatherResponse
}