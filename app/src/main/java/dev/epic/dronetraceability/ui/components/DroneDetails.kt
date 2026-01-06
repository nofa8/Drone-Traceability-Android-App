package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.epic.dronetraceability.DroneScreen
import dev.epic.dronetraceability.R
import dev.epic.dronetraceability.data.model.domain.Drone
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneDetails(
    drone: Drone,
    padding: PaddingValues,
    navController: NavController
) {
    val t = drone.telemetry

    // Scrollable Column
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* --- IDENTIFICAÇÃO --- */
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = drone.model,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "ID: ${drone.droneId}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (drone.isConnected) "Connected" else "Disconnected",
                color = if (drone.isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Divider(thickness = 1.dp)

        /* --- LOCALIZAÇÃO --- */
        SectionCentered(title = "Location") {
            KeyValueCentered("Latitude", "%.5f".format(t.latitude))
            KeyValueCentered("Longitude", "%.5f".format(t.longitude))
            KeyValueCentered("Altitude", "${"%.1f".format(t.altitude)} m")
            KeyValueCentered("Heading", "${"%.1f".format(t.heading)}°")
        }

        /* --- MOVIMENTO --- */
        SectionCentered(title = "Movement") {
            KeyValueCentered(
                "Velocity",
                "X ${"%.2f".format(t.velocityX)}, Y ${"%.2f".format(t.velocityY)}, Z ${"%.2f".format(t.velocityZ)}"
            )
        }

        /* --- ENERGIA --- */
        SectionCentered(title = "Energy") {
            KeyValueCentered("Battery", "${"%.1f".format(t.batteryLevel)} %")
            KeyValueCentered("Battery Temp", "${"%.1f".format(t.batteryTemperature)} °C")
            KeyValueCentered("Remaining Flight Time", "${t.remainingFlightTime} s")
        }

        /* --- ESTADO --- */
        SectionCentered(title = "Status") {
            KeyValueCentered("Flying", yesNo(t.isFlying))
            KeyValueCentered("Traveling", yesNo(t.isTraveling))
            KeyValueCentered("Going Home", yesNo(t.isGoingHome))
            KeyValueCentered("Online", yesNo(t.online))
            KeyValueCentered("Motors", if (t.areMotorsOn) "On" else "Off")
            KeyValueCentered("Lights", if (t.areLightsOn) "On" else "Off")
            KeyValueCentered("Satellites", t.satelliteCount.toString())
        }

        /* --- HOME LOCATION --- */
        t.homeLocation?.let {
            SectionCentered(title = "Home Location") {
                KeyValueCentered("Latitude", "%.5f".format(it.latitude))
                KeyValueCentered("Longitude", "%.5f".format(it.longitude))
            }
        }

        /* --- IMAGEM DO DRONE --- */
        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.drone),
            contentDescription = "Drone Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        /* --- BOTÃO --- */
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(DroneScreen.Map.createRoute(drone.droneId)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Map")
        }
    }
}

/* --------------------- SEÇÃO CENTRALIZADA --------------------- */
@Composable
private fun SectionCentered(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Divider(modifier = Modifier.padding(vertical = 4.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Composable
private fun KeyValueCentered(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun yesNo(value: Boolean) = if (value) "Yes" else "No"
