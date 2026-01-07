package dev.epic.dronetraceability.ui.dronedetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.epic.dronetraceability.navigation.RepositoryProvider
import dev.epic.dronetraceability.ui.components.DroneDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneDetailScreen(
    navController: NavController,
    droneId: String,
    viewModel: DroneDetailViewModel = viewModel(
        factory = DroneDetailViewModelFactory(droneId, RepositoryProvider.droneRepository)
    )
) {
    val drone by viewModel.drone.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Drone Details")
                },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                error != null -> {
                    Text(
                        text = "Erro: $error",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                drone == null && isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                drone != null -> {
                    DroneDetails(
                        drone = drone!!,
                        navController = navController
                    )
                }

                else -> {
                    Text(
                        text = "Drone não disponível",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

