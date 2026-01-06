package dev.epic.dronetraceability.ui.dronedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.epic.dronetraceability.data.model.domain.Drone
import dev.epic.dronetraceability.data.repository.DroneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class DroneDetailViewModel(
    private val droneId: String,
    private val repo: DroneRepository
) : ViewModel() {

    val drone: StateFlow<Drone?> =
        repo.drones
            .map { drones -> drones[droneId] }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val isLoading: StateFlow<Boolean> = repo.isLoading
    val error: StateFlow<String?> = repo.error
}
