package dev.epic.dronetraceability.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.epic.dronetraceability.data.api.ApiClient
import dev.epic.dronetraceability.ui.components.TelemetryHistoryCard
import java.time.Instant
import androidx.compose.foundation.lazy.items


@kotlin.time.ExperimentalTime
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneHistoryScreen(
    navController: NavController,
    droneId: String,
    viewModel: DroneHistoryViewModel = viewModel(
        factory = DroneHistoryViewModelFactory(droneId, ApiClient.api)
    )
) {
    val state by viewModel.state.collectAsState()

    val fromDate = remember { mutableStateOf<String?>(null) }
    val toDate = remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Drone $droneId History") },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Filtros de data
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = fromDate.value ?: "",
                    onValueChange = { fromDate.value = it },
                    label = { Text("From (yyyy-MM-dd)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = toDate.value ?: "",
                    onValueChange = { toDate.value = it },
                    label = { Text("To (yyyy-MM-dd)") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    val fromInstant = fromDate.value?.let { Instant.parse("${it}T00:00:00Z") }
                    val toInstant = toDate.value?.let { Instant.parse("${it}T23:59:59Z") }
                    viewModel.setFilters(fromInstant, toInstant)
                }) {
                    Text("Apply")
                }
            }

            // Paginação + Refresh
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = viewModel::loadNewer,
                    enabled = state.nextCursor != null && !state.isLoading && state.items.isNotEmpty()
                ) {
                    Text("Newer")
                }

                Button(
                    onClick = { viewModel.loadInitial() }, // botao de refresh
                    enabled = !state.isLoading
                ) {
                    Text("Refresh")
                }

                Button(
                    onClick = viewModel::loadOlder,
                    enabled = state.prevCursor != null && !state.isLoading && state.items.isNotEmpty()
                ) {
                    Text("Older")
                }
            }

            // Listagem de telemetria
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.items) { telemetry ->
                    TelemetryHistoryCard(telemetry)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


