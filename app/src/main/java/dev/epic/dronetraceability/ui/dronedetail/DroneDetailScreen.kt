package dev.epic.dronetraceability.ui.dronedetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import dev.epic.dronetraceability.ui.components.DroneDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneDetailScreen(navController: NavController, droneId: Long) {
    val viewModel: DroneDetailViewModel = viewModel(
        factory = DroneDetailViewModelFactory(droneId)
    )

    val drone by viewModel.drone.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(drone?.model ?: "Drone Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) { padding ->
        drone?.let {
            DroneDetails(drone!!, padding, navController )
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}


