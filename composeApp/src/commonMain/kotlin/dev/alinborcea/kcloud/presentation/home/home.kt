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
import dev.alinborcea.kcloud.presentation.home.components.QuickForecast
import dev.alinborcea.kcloud.presentation.home.components.SearchBar
import dev.alinborcea.kcloud.presentation.home.components.WeatherCard
import dev.alinborcea.kcloud.presentation.home.components.getDayName
import dev.alinborcea.kcloud.presentation.home.sections.Chatbot
import dev.alinborcea.kcloud.presentation.home.sections.HourlyForecast
import dev.alinborcea.kcloud.presentation.home.sections.Settings

@Composable
fun Home(viewModel: HomeViewModel) {
    var selectedSection by remember { mutableIntStateOf(0) }
    val sectionNames = listOf("Home", "Questions", "Settings")
    val sectionIcons =
        listOf(Icons.Default.Home, Icons.Default.QuestionAnswer, Icons.Default.Settings)

    var forecastDayIndex by remember { mutableIntStateOf(-1) }

    var userSettings by remember { mutableStateOf(viewModel.loadSettings()) }
    var weatherInfo by remember {
        mutableStateOf(
            viewModel.updatedCurrentWeatherResponse(userSettings.favoriteLocation)
        )
    }
    var forecast by remember {
        mutableStateOf(
            viewModel.updatedWeatherForecastResponse(userSettings.favoriteLocation)
        )
    }

    Scaffold(bottomBar = {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
            sectionNames.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(sectionIcons[index], contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedSection == index,
                    onClick = { selectedSection = index }
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
            if (forecastDayIndex == -1) {
                when (selectedSection) {
                    0 -> {
                        SearchBar(
                            query = userSettings.favoriteLocation,
                            onSearch = { city ->
                                weatherInfo = viewModel.updatedCurrentWeatherResponse(city)
                                forecast = viewModel.updatedWeatherForecastResponse(city)
                            }
                        )

                        WeatherCard(data = weatherInfo, userSettings = userSettings)
                        QuickForecast(
                            forecast.forecast.forecastDay,
                            userSettings = userSettings,
                            onClickItem = { forecastDayIndex = it })

                    }
                    1 -> {
                        Chatbot(userSettings.favoriteLocation)

                    }
                    2 -> {
                        Settings(
                            settings = userSettings,
                            onSettingsChanged = { updated ->
                                userSettings = updated
                                viewModel.saveSettings(updated)
                                weatherInfo =
                                    viewModel.updatedCurrentWeatherResponse(userSettings.favoriteLocation)
                                forecast =
                                    viewModel.updatedWeatherForecastResponse(userSettings.favoriteLocation)
                            }
                        )
                    }
                }
            } else {
                if (forecast.forecast.forecastDay.size != 3) {
                    Text("No items here!")
                } else {

                    val hours = when (forecastDayIndex) {
                        0 -> forecast.forecast.forecastDay[0].hour
                        1 -> forecast.forecast.forecastDay[1].hour
                        2 -> forecast.forecast.forecastDay[2].hour
                        else -> forecast.forecast.forecastDay[0].hour
                    }

                    val dayName = when (forecastDayIndex) {
                        0 -> forecast.forecast.forecastDay[0].date
                        1 -> forecast.forecast.forecastDay[1].date
                        2 -> forecast.forecast.forecastDay[2].date
                        else -> forecast.forecast.forecastDay[0].date
                    }

                    HourlyForecast(
                        hours,
                        userSettings.useMetric,
                        getDayName(dayName),
                        onCallBack = { forecastDayIndex = -1 })
                }
            }
        }
    }
}