package dev.alinborcea.kcloud.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.alinborcea.kcloud.domain.models.ForecastDay
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

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
                text = getDayName(forecastDay.date), // Simple slice for now
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

fun getDayName(dateString: String): String {
    // 1. Parse the ISO-8601 string (YYYY-MM-DD)
    val date = LocalDate.parse(dateString)

    // 2. Get the DayOfWeek enum
    val dayOfWeek: DayOfWeek = date.dayOfWeek

    // 3. Return the name (e.g., "WEDNESDAY")
    // Use .name.lowercase().replaceFirstChar { it.uppercase() } for "Wednesday"
    val dayMap: Map<String, String> = mapOf(
        "MONDAY" to "Mon",
        "TUESDAY" to "Tue",
        "WEDNESDAY" to "Wed",
        "THURSDAY" to "Thu",
        "FRIDAY" to "Fri",
        "SATURDAY" to "Sat",
        "SUNDAY" to "Sun"
    )
    return dayMap[dayOfWeek.name] ?: ""
}