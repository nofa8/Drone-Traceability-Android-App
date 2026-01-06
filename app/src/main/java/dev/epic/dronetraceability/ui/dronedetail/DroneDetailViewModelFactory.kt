package dev.epic.dronetraceability.ui.dronedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.epic.dronetraceability.data.repository.DroneRepository

class DroneDetailViewModelFactory(
    private val droneId: String,
    private val repository: DroneRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DroneDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DroneDetailViewModel(
                droneId = droneId,
                repo = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

