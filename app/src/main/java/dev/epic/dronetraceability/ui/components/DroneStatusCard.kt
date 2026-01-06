package dev.epic.dronetraceability.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.epic.dronetraceability.data.model.domain.Drone

@Composable
fun DroneStatusCard(drone: Drone, modifier: Modifier = Modifier) {
    val targetProgress = drone.telemetry.batteryLevel / 100f
    val animatedProgress by animateFloatAsState(targetValue = targetProgress.toFloat())

    val batColor = when {
        drone.telemetry.batteryLevel > 70 -> Color(0xFF4CAF50)
        drone.telemetry.batteryLevel > 40 -> Color(0xFFFFC107)
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = modifier.fillMaxWidth(0.95f),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                StatusItem(label = "Altitude", value = "${"%.1f".format(drone.telemetry.altitude)} m")
                StatusItem(label = "Velocity", value = "${"%.1f".format(Math.sqrt(Math.pow(drone.telemetry.velocityX, 2.0)+ Math.pow(drone.telemetry.velocityY, 2.0) + Math.pow(drone.telemetry.velocityZ, 2.0) ))} m/s")
                StatusItem(label = "Status", value = if (drone.telemetry.isFlying) "Flying" else "Grounded")
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Battery:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(MaterialTheme.shapes.small),
                    color = batColor,
                    trackColor = batColor.copy(alpha = 0.25f),
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("${drone.telemetry.batteryLevel}%", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = batColor)
            }
        }
    }
}