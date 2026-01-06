package dev.epic.dronetraceability.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.epic.dronetraceability.data.repository.DroneRepository

class DroneMapViewModelFactory(
    private val droneId: String,
    private val repository: DroneRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DroneMapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DroneMapViewModel(droneId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
