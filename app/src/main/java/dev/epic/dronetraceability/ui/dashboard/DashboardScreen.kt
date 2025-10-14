package dev.epic.dronetraceability.ui.dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.epic.dronetraceability.DroneScreen
import dev.epic.dronetraceability.ui.components.DroneCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel = viewModel()) {
    val drones by  viewModel.drones.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Drone Dashboard") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(drones) { drone ->
                DroneCard(drone) {
                    navController.navigate(DroneScreen.Detail.createRoute(drone.id))
                }
            }
        }
    }
}


