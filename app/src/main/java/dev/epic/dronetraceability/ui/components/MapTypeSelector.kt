package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MapType

@Composable
fun MapTypeSelector(
    currentType: MapType,
    onTypeSelected: (MapType) -> Unit,
    modifier: Modifier = Modifier
) {
    val mapTypes = remember { listOf(MapType.HYBRID, MapType.SATELLITE, MapType.NORMAL) }

    // Using a Chip-like Row for a modern, segmented look
    Row(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        mapTypes.forEach { type ->
            val isSelected = type == currentType
            val text = when (type) {
                MapType.HYBRID -> "Hybrid"
                MapType.SATELLITE -> "Satellite"
                else -> "Normal"
            }

            AssistChip(
                onClick = { onTypeSelected(type) },
                label = { Text(text) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    labelColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ),
                border = null,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}