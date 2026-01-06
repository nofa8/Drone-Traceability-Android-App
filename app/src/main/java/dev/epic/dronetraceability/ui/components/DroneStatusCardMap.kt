package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.epic.dronetraceability.data.model.domain.Telemetry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneStatusCardMap(
    droneId: String,
    telemetry: Telemetry,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        // onclick?
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Drone $droneId",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Battery: ${"%.1f".format(telemetry.batteryLevel)}%",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Altitude: ${"%.1f".format(telemetry.altitude)} m",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Satellites: ${telemetry.satelliteCount}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Heading: ${"%.1f".format(telemetry.heading)}°",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${if (telemetry.isFlying) "Flying" else "Idle"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
