package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.epic.dronetraceability.data.model.domain.Telemetry
import java.time.Instant
import java.time.ZoneId
import java.util.Locale


@Composable
fun TelemetryHistoryCard(
    telemetry: Telemetry
) {
    val instant = runCatching { Instant.parse(telemetry.timestamp) }.getOrNull()
    val zoned = instant?.atZone(ZoneId.systemDefault())

    val dateText = zoned?.toLocalDate()?.toString() ?: "Unknown date"
    val timeText = zoned?.toLocalTime()?.let {
        String.format(
            Locale.US,
            "%02dh %02dm %02ds",
            it.hour,
            it.minute,
            it.second
        )
    } ?: "Unknown time"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Date: $dateText   •   Time: $timeText",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )


            HorizontalDivider()

            SectionTitle("Position")
            TwoColumnRow(
                "Latitude", "%.5f".format(Locale.US, telemetry.latitude),
                "Longitude", "%.5f".format(Locale.US, telemetry.longitude)
            )

            HorizontalDivider()

            SectionTitle("Altitude & Heading")
            ThreeColumnRow(
                "Altitude", "%.1f m".format(Locale.US, telemetry.altitude),
                "Heading", "%.1f°".format(Locale.US, telemetry.heading),
                "Satellites", telemetry.satelliteCount.toString()
            )

            HorizontalDivider()


            SectionTitle("Velocity (m/s)")

            ThreeColumnRow(
                "X", "%.2f".format(Locale.US, telemetry.velocityX),
                "Y", "%.2f".format(Locale.US, telemetry.velocityY),
                "Z", "%.2f".format(Locale.US, telemetry.velocityZ),)

            HorizontalDivider()

            SectionTitle("Status")
            TwoColumnRow(
                "Battery", "%.1f %%".format(Locale.US, telemetry.batteryLevel),
                "Flying", yesNo(telemetry.isFlying),
            )

            TwoColumnRow(
                "Going Home", yesNo(telemetry.isGoingHome),
                "Motors", yesNo(telemetry.areMotorsOn)
            )


        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium
    )
}


@Composable
private fun TwoColumnRow(
    label1: String, value1: String,
    label2: String, value2: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.widthIn(max = 320.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBlock(label1, value1)
            Spacer(modifier = Modifier.width(24.dp))
            InfoBlock(label2, value2)
        }
    }
}


@Composable
private fun ThreeColumnRow(
    label1: String, value1: String,
    label2: String, value2: String,
    label3: String, value3: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.widthIn(max = 360.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBlock(label1, value1)
            Spacer(modifier = Modifier.width(20.dp))
            InfoBlock(label2, value2)
            Spacer(modifier = Modifier.width(20.dp))
            InfoBlock(label3, value3)
        }
    }
}


@Composable
private fun InfoBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun yesNo(value: Boolean): String =
    if (value) "Yes" else "No"


