package dev.alinborcea.kcloud.presentation.home.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.alinborcea.kcloud.domain.models.UserSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    settings: UserSettings,
    onSettingsChanged: (UserSettings) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Preferences",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // --- Favorite Location Section ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Favorite Location", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = settings.favoriteLocation,
                onValueChange = {
                    onSettingsChanged(settings.copy(favoriteLocation = it))
                },
                placeholder = { Text("e.g. Tokyo, JP") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        // --- Unit of Measurement Section ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Unit of Measurement", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Metric Option
                UnitOption(
                    label = "Metric (°C, km/h)",
                    selected = settings.useMetric,
                    onClick = { onSettingsChanged(settings.copy(useMetric = true)) },
                    modifier = Modifier.weight(1f)
                )
                // Imperial Option
                UnitOption(
                    label = "Imperial (°F, mph)",
                    selected = !settings.useMetric,
                    onClick = { onSettingsChanged(settings.copy(useMetric = false)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun UnitOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = modifier.height(60.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}