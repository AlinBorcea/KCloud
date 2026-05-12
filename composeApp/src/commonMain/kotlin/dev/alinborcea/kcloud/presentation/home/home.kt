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
import dev.alinborcea.kcloud.domain.models.UserSettings
import dev.alinborcea.kcloud.domain.models.WeatherResponse
import kotlinx.coroutines.runBlocking

@Composable
fun HomePage() {
    val weather = WeatherAPI()

    var weatherInfo by remember { mutableStateOf(WeatherResponse()) }
    var forecast by remember { mutableStateOf(WeatherResponse()) }

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Forecast", "Settings")
    val icons = listOf(Icons.Default.Home, Icons.Default.Cloud, Icons.Default.Settings)

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

            } else if (selectedItem == 2) {
                SettingsScreen(settings = UserSettings())
            }
        }
    }


}