package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.epic.dronetraceability.DroneScreen
import dev.epic.dronetraceability.R
import dev.epic.dronetraceability.data.model.domain.Drone
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneDetails(
    drone: Drone,
    navController: NavController
) {
    val t = drone.telemetry

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ){

        /* --- IDENTIFICAÇÃO --- */
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = drone.model,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "ID: ${drone.droneId}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (drone.isConnected) "Connected" else "Disconnected",
                color = if (drone.isConnected) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))

        /* --- STATUS / ENERGY / LOCALIZAÇÃO HORIZONTAL --- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionHorizontal(
                title = "Status",
                content = {
                    KeyValueHorizontal("Flying", yesNo(t.isFlying))
                    KeyValueHorizontal("Traveling", yesNo(t.isTraveling))
                    KeyValueHorizontal("Motors", if (t.areMotorsOn) "On" else "Off")
                    KeyValueHorizontal("Lights", if (t.areLightsOn) "On" else "Off")
                    KeyValueHorizontal("Going Home", yesNo(t.isGoingHome))
                },
                modifier = Modifier.weight(1f)
            )

            SectionHorizontal(
                title = "Energy",
                content = {
                    KeyValueHorizontal("Battery", "${"%.1f".format(t.batteryLevel)} %")
                    KeyValueHorizontal("Battery Temp", "${"%.1f".format(t.batteryTemperature)} °C")
                    KeyValueHorizontal("Remaining Flight Time", "${t.remainingFlightTime} s")
                },
                modifier = Modifier.weight(1f)
            )

            SectionHorizontal(
                title = "Location",
                content = {
                    KeyValueHorizontal("Latitude", "%.5f".format(t.latitude))
                    KeyValueHorizontal("Longitude", "%.5f".format(t.longitude))
                    KeyValueHorizontal("Altitude", "${"%.1f".format(t.altitude)} m")
                    KeyValueHorizontal("Heading", "${"%.1f".format(t.heading)}°")
                    KeyValueHorizontal("Satellites", t.satelliteCount.toString())
                },
                modifier = Modifier.weight(1f)
            )
        }


        /* --- MOVIMENTO --- */
        SectionCentered(title = "Movement") {
            KeyValueCentered(
                "Velocity",
                "X ${"%.2f".format(t.velocityX)}, Y ${"%.2f".format(t.velocityY)}, Z ${"%.2f".format(t.velocityZ)}"
            )
        }


        /* --- HOME LOCATION --- */
        t.homeLocation?.let {
            SectionCentered(title = "Home Location") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Latitude", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                        Text("%.5f".format(it.latitude), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Longitude", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                        Text("%.5f".format(it.longitude), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                    }
                }
            }
        }


        /* --- IMAGEM DO DRONE --- */
        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.drone),
            contentDescription = "Drone Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        )

        /* --- BOTÃO --- */
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(DroneScreen.Map.createRoute(drone.droneId)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Map", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}

/* --------------------- SEÇÃO HORIZONTAL ESTILIZADA --------------------- */
@Composable
private fun SectionHorizontal(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

@Composable
private fun KeyValueHorizontal(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
        Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
    }
}

/* --------------------- SEÇÃO CENTRALIZADA ESTILIZADA --------------------- */
@Composable
private fun SectionCentered(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

@Composable
private fun KeyValueCentered(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
        Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
    }
}

private fun yesNo(value: Boolean) = if (value) "Yes" else "No"

