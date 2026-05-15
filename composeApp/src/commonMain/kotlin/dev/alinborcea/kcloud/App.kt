package dev.alinborcea.kcloud

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import dev.alinborcea.kcloud.data.repositories.WeatherAPI
import dev.alinborcea.kcloud.data.services.SettingsManager
import dev.alinborcea.kcloud.presentation.home.Home
import dev.alinborcea.kcloud.presentation.home.HomeViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        Home(HomeViewModel(WeatherAPI(), SettingsManager()))
    }
}