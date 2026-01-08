package dev.epic.dronetraceability.ui.commands

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.epic.dronetraceability.data.repository.DroneRepository

class DroneCommandViewModelFactory(
    private val droneId: String,
    private val repo: DroneRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DroneCommandViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DroneCommandViewModel(droneId, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

