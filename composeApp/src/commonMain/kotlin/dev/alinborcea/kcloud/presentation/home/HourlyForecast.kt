package dev.alinborcea.kcloud.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.alinborcea.kcloud.domain.models.Hour

@Composable
fun HourlyForecast(
    hours: List<Hour>,
    useMetric: Boolean,
    dayName: String,
    onCallBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row {
            IconButton(
                onClick = onCallBack,
                modifier = Modifier
                    .padding(16.dp)
                    //.align(Alignment.TopStart)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(dayName, modifier = Modifier.padding(16.dp))
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(hours) { hour ->
            HourItem(hour, useMetric)
        }
    }
}

@Composable
fun HourItem(hour: Hour, useMetric: Boolean) {
    // Extracting just the time (HH:mm) from "YYYY-MM-DD HH:mm"
    val timeDisplay = hour.time.split(" ").lastOrNull() ?: ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
            .width(60.dp)
    ) {
        Text(
            text = timeDisplay,
            style = MaterialTheme.typography.labelMedium
        )

        Text(hour.condition.text, style = MaterialTheme.typography.bodySmall)

        Text(
            text = if (useMetric) "${hour.tempC.toInt()}°C" else "${hour.tempF.toInt()}°F",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}