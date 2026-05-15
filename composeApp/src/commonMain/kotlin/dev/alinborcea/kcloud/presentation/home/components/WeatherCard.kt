package dev.alinborcea.kcloud.presentation.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import dev.alinborcea.kcloud.domain.models.UserSettings
import dev.alinborcea.kcloud.domain.models.WeatherResponse

@Composable
fun WeatherCard(
    data: WeatherResponse,
    modifier: Modifier = Modifier,
    userSettings: UserSettings
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
                    text = "${data.current.temperature(userSettings.useMetric)}°",
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
                        text = "Feels like ${data.current.feelsLike(userSettings.useMetric)}°${if (userSettings.useMetric) "C" else "F"}",
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
                    value = "${data.current.wind(userSettings.useMetric)} ${if (userSettings.useMetric) "km/h" else "m/h"}",
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
                            value = "${data.current.pressure(userSettings.useMetric)} ${if (userSettings.useMetric) "hPa" else "in"}",
                            icon = Icons.Default.Compress
                        )
                        WeatherDetailItem(
                            label = "UV Index",
                            value = "${data.current.uv}",
                            icon = Icons.Default.WbSunny
                        )
                        WeatherDetailItem(
                            label = "Visibility",
                            value = "${data.current.visibility(userSettings.useMetric)} ${if (userSettings.useMetric) "km" else "mi"}",
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
                            value = "${data.current.gust(userSettings.useMetric)} ${if (userSettings.useMetric) "kph" else "mph"}",
                            icon = Icons.Default.WindPower
                        )
                    }
                }
            }
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