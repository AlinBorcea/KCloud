package dev.alinborcea.kcloud.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionAnswer
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
import dev.alinborcea.kcloud.presentation.home.components.QuickForecast
import dev.alinborcea.kcloud.presentation.home.components.SearchBar
import dev.alinborcea.kcloud.presentation.home.components.WeatherCard
import dev.alinborcea.kcloud.presentation.home.components.getDayName
import dev.alinborcea.kcloud.presentation.home.sections.Chatbot
import dev.alinborcea.kcloud.presentation.home.sections.HourlyForecast
import dev.alinborcea.kcloud.presentation.home.sections.Settings
import kotlinx.coroutines.runBlocking

@Composable
fun Home(weather: WeatherAPI, settingsManager: SettingsManager) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Questions", "Settings")
    val icons = listOf(Icons.Default.Home, Icons.Default.QuestionAnswer, Icons.Default.Settings)

    var forecastIndex by remember { mutableIntStateOf(-1) }

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
            if (forecastIndex == -1) {
                if (selectedItem == 0) {
                    SearchBar(
                        query = userSettings.favoriteLocation,
                        onSearch = { city ->
                            weatherInfo = updatedCurrentWeatherResponse(weather, city)
                            forecast = updatedWeatherForecastResponse(weather, city)
                        }
                    )

                    WeatherCard(data = weatherInfo, userSettings = userSettings)
                    QuickForecast(
                        forecast.forecast.forecastDay,
                        userSettings = userSettings,
                        onClickItem = { forecastIndex = it })

                } else if (selectedItem == 1) {
                    Chatbot(userSettings.favoriteLocation)

                } else if (selectedItem == 2) {
                    Settings(
                        settings = userSettings,
                        onSettingsChanged = { updated ->
                            userSettings = updated
                            settingsManager.saveSettings(updated)
                            weatherInfo =
                                updatedCurrentWeatherResponse(
                                    weather,
                                    userSettings.favoriteLocation
                                )
                            forecast =
                                updatedWeatherForecastResponse(
                                    weather,
                                    userSettings.favoriteLocation
                                )
                        }
                    )
                }
            } else {
                if (forecast.forecast.forecastDay.size != 3) {
                    Text("No items here!")
                } else {

                    val hours = when (forecastIndex) {
                        0 -> forecast.forecast.forecastDay[0].hour
                        1 -> forecast.forecast.forecastDay[1].hour
                        2 -> forecast.forecast.forecastDay[2].hour
                        else -> forecast.forecast.forecastDay[0].hour
                    }

                    val dayName = when (forecastIndex) {
                        0 -> forecast.forecast.forecastDay[0].date
                        1 -> forecast.forecast.forecastDay[1].date
                        2 -> forecast.forecast.forecastDay[2].date
                        else -> forecast.forecast.forecastDay[0].date
                    }

                    HourlyForecast(
                        hours,
                        userSettings.useMetric,
                        getDayName(dayName),
                        onCallBack = { forecastIndex = -1 })
                }
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
            weatherResponse = weatherAPI.getWeatherForecast(location, 3)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return weatherResponse
}