package dev.epic.dronetraceability.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.epic.dronetraceability.DroneScreen
import dev.epic.dronetraceability.navigation.RepositoryProvider
import dev.epic.dronetraceability.ui.components.DroneCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory(RepositoryProvider.droneRepository))
) {
    val dronesMap by viewModel.drones.collectAsState()
    val drones = dronesMap.values.sortedBy { drone -> drone.droneId }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Drone Dashboard") })
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Erro: $error")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(
                        items = drones,
                        key = { it.droneId } // key, essencial para ws (garante que Compose não recria cartões desnecessariamente)
                    ) { drone ->
                        DroneCard(drone) {
                            navController.navigate(
                                DroneScreen.Detail.createRoute(drone.droneId)
                            )
                        }
                    }
                }
            }
        }
    }
}
