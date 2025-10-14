package dev.epic.dronetraceability.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.epic.dronetraceability.data.model.Drone
import dev.epic.dronetraceability.data.repository.DroneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DashboardViewModel : ViewModel() {
    private val repo = DroneRepository()

    private val _drones = MutableStateFlow<List<Drone>>(emptyList())
    val drones: StateFlow<List<Drone>> = _drones

    init {
        viewModelScope.launch {
            repo.getDrones().collect { droneList ->
                _drones.value = droneList
            }
        }
    }
}
