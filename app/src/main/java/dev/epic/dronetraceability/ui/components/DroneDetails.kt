package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.epic.dronetraceability.DroneScreen
import dev.epic.dronetraceability.data.model.Drone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneDetails (drone: Drone, padding: PaddingValues, navController: NavController) {
    Column(modifier = Modifier.padding(padding).padding(16.dp)) {
        Text("Model: ${drone.model}")
        Text("Battery: ${drone.batLvl}%")
        Text("Altitude: ${"%.1f".format(drone.alt)} m")
        Text("Heading: ${"%.1f".format(drone.hdg)}°")
        Text("Flying: ${if (drone.isFlying) "Yes" else "No"}")
        Text("Motors: ${if (drone.areMotorsOn) "On" else "Off"}")

        Spacer(Modifier.height(16.dp))

        Button(onClick = { navController.navigate(DroneScreen.Map.createRoute(drone.id)) }) {
            Text("View Map")
        }
    }
}