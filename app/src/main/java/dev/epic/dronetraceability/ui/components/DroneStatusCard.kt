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
import androidx.compose.material3.Divider
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
import dev.epic.dronetraceability.data.model.Drone

@Composable
fun DroneStatusCard(drone: Drone, modifier: Modifier = Modifier) {
    val targetProgress = drone.batLvl / 100f
    val animatedProgress by animateFloatAsState(targetValue = targetProgress)

    val batColor = when {
        drone.batLvl > 70 -> Color(0xFF4CAF50)
        drone.batLvl > 40 -> Color(0xFFFFC107)
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
                StatusItem(label = "Altitude", value = "${"%.1f".format(drone.alt)} m")
                StatusItem(label = "Velocity", value = "${"%.1f".format(Math.sqrt(Math.pow(drone.velX, 2.0)+ Math.pow(drone.velY, 2.0) + Math.pow(drone.velZ, 2.0) ))} m/s")
                StatusItem(label = "Status", value = if (drone.isFlying) "Flying" else "Grounded")
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
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
                Text("${drone.batLvl}%", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = batColor)
            }
        }
    }
}