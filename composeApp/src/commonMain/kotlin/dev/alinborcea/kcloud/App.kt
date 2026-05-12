package dev.alinborcea.kcloud

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import dev.alinborcea.kcloud.data.repositories.WeatherAPI
import dev.alinborcea.kcloud.data.services.SettingsManager
import dev.alinborcea.kcloud.presentation.home.HomePage

@Composable
@Preview
fun App() {
    val weather = WeatherAPI()
    val settingsManager = remember { SettingsManager() }

    MaterialTheme {
        HomePage(weather = weather, settingsManager = settingsManager)
    }
}