package dev.alinborcea.kcloud.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alinborcea.kcloud.data.repositories.WeatherAPI
import dev.alinborcea.kcloud.domain.models.ForecastDay
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(
    query: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var active by remember { mutableStateOf(false) }
    var locationQuery by remember { mutableStateOf(query) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = locationQuery,
            onQueryChange = { locationQuery = it },
            onSearch = {
                active = false
                onSearch(it)
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text("Search city (e.g. London)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (active && query.isNotEmpty()) {
                    TextButton(onClick = { active = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(28.dp),
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            //Favourite locations
        }
    }
}

@Composable
fun WeatherSummaryCard(
    data: WeatherResponse,
    modifier: Modifier = Modifier
) {
    var extraInfoExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { extraInfoExpanded = !extraInfoExpanded },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            // --- Header: Location and Country ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = data.location.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = data.location.country,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Local Time (Stripping the date if needed)
                Text(
                    text = data.location.localtime.substringAfter(" "),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Main Section: Temp and Condition ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${data.current.tempC.toInt()}°",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 72.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = data.current.condition.text,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Feels like ${data.current.feelslikeC.toInt()}°C",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Footer: Wind and Humidity ---
            // Horizontal rule to separate the "big numbers" from technical details
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetailItem(
                    label = "Wind",
                    value = "${data.current.windKph} km/h",
                    icon = Icons.Default.Air // If using Material Icons
                )
                WeatherDetailItem(
                    label = "Humidity",
                    value = "${data.current.humidity}%",
                    icon = Icons.Default.WaterDrop
                )
            }
            // --- Expandable Section: Remaining Data ---
            AnimatedVisibility(visible = extraInfoExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(24.dp))

                    // First row of extra details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WeatherDetailItem(
                            label = "Pressure",
                            value = "${data.current.pressureMb} hPa",
                            icon = Icons.Default.Compress
                        )
                        WeatherDetailItem(
                            label = "UV Index",
                            value = "${data.current.uv}",
                            icon = Icons.Default.WbSunny
                        )
                        WeatherDetailItem(
                            label = "Visibility",
                            value = "${data.current.visKm} km",
                            icon = Icons.Default.Visibility
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Second row of extra details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WeatherDetailItem(
                            label = "Cloud",
                            value = "${data.current.cloud}%",
                            icon = Icons.Default.Cloud
                        )
                        WeatherDetailItem(
                            label = "Rain Chance",
                            value = "${data.current.chanceOfRain}%",
                            icon = Icons.Default.Umbrella
                        )
                        WeatherDetailItem(
                            label = "Gusts",
                            value = "${data.current.gustKph} kph",
                            icon = Icons.Default.WindPower
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastSection(forecastDays: List<ForecastDay>) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            text = "Forecast",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(forecastDays) { day ->
                ForecastItem(day)
            }
        }
    }
}

@Composable
fun ForecastItem(forecastDay: ForecastDay) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Helper to get day name (e.g., "Mon") from "2023-10-25"
            Text(
                text = forecastDay.date.takeLast(5), // Simple slice for now
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Icon Placeholder (You can use Coil or KMP library to load the URL)
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${forecastDay.day.maxTempC.toInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${forecastDay.day.minTempC.toInt()}°",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeatherDetailItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}