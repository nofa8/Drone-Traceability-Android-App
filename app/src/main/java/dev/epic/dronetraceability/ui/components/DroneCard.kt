package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.epic.dronetraceability.data.model.domain.Drone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneCard(drone: Drone, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(drone.model, style = MaterialTheme.typography.titleMedium)
            Text("Battery: ${drone.telemetry.batteryLevel}%", style = MaterialTheme.typography.bodyMedium)
            Text("Altitude: ${"%.1f".format(drone.telemetry.altitude)} m", style = MaterialTheme.typography.bodyMedium)
            Text("Satellites: ${drone.telemetry.satelliteCount}", style = MaterialTheme.typography.bodyMedium)
            Text("Heading: ${"%.1f".format(drone.telemetry.heading)}°", style = MaterialTheme.typography.bodyMedium)
            Text("Status: ${if (drone.telemetry.isFlying) "Flying" else "Idle"}", style = MaterialTheme.typography.bodyMedium)
            Text("Connection Status: ${if (drone.isConnected) "Connected" else "Disconnected"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
