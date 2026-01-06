package dev.epic.dronetraceability.ui.map

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import dev.epic.dronetraceability.data.model.domain.Telemetry
import dev.epic.dronetraceability.data.repository.DroneRepository
import kotlinx.coroutines.flow.*

class DroneMapViewModel(
    private val droneId: String,
    private val repo: DroneRepository
) : ViewModel() {

    val telemetry: StateFlow<Telemetry?> = repo.drones
        .map { drones -> drones[droneId]?.telemetry }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val isLoading: StateFlow<Boolean> = repo.isLoading

    val error: StateFlow<String?> = repo.error
}
