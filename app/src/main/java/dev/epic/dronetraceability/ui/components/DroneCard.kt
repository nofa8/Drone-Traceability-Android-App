package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.epic.dronetraceability.data.model.domain.Drone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneCard(drone: Drone, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFFE6F0FF)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Linha do modelo à esquerda e status à direita
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = drone.model,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = if (drone.isConnected) "Connected" else "Disconnected",
                    color = if (drone.isConnected) androidx.compose.ui.graphics.Color(0xFF4CAF50)
                    else androidx.compose.ui.graphics.Color(0xFFF44336),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Valores principais agrupados à esquerda e à direita
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoRowBold(label = "Battery   ", value = "${"%.1f".format(drone.telemetry.batteryLevel)} %")
                    InfoRowBold(label = "Altitude  ", value = "${"%.1f".format(drone.telemetry.altitude)} m")
                }
                Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                    InfoRowBold(label = "Latitude   ", value = "%.2f".format(drone.telemetry.latitude))
                    InfoRowBold(label = "Longitude  ", value = "%.2f".format(drone.telemetry.longitude))
                }
                Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                    InfoRowBold(label = "Motors  ", value = if (drone.telemetry.areMotorsOn) "ON" else "OFF")
                    InfoRowBold(label = "Lights  ", value = if (drone.telemetry.areLightsOn) "ON" else "OFF")
                }

            }
        }
    }
}

@Composable
private fun InfoRowBold(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


