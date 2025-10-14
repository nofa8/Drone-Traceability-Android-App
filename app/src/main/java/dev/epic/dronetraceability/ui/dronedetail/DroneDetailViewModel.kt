package dev.epic.dronetraceability.ui.dronedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.epic.dronetraceability.data.model.Drone
import dev.epic.dronetraceability.data.repository.DroneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DroneDetailViewModel(
    private val droneId: Long,
    private val repo: DroneRepository = DroneRepository()
) : ViewModel() {

    private val _drone = MutableStateFlow<Drone?>(null)
    val drone: StateFlow<Drone?> = _drone

    init {
        viewModelScope.launch {
            repo.getDrones().collect { drones ->
                _drone.value = drones.find { it.id == droneId }
            }
        }
    }
}


class DroneDetailViewModelFactory(
    private val droneId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DroneDetailViewModel::class.java)) {
            return DroneDetailViewModel(droneId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
