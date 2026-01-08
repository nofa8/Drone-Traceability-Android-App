package dev.epic.dronetraceability.ui.commands

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.epic.dronetraceability.navigation.RepositoryProvider
import dev.epic.dronetraceability.ui.components.DroneCommandContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneCommandScreen(
    navController: NavController,
    droneId: String,
    viewModel: DroneCommandViewModel = viewModel(
        factory = DroneCommandViewModelFactory(
            droneId,
            RepositoryProvider.droneRepository
        )
    )
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Drone Commands") },
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

        DroneCommandContent(
            modifier = Modifier.padding(padding),
            viewModel = viewModel
        )
    }
}

