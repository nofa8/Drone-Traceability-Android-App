package dev.epic.dronetraceability.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.epic.dronetraceability.data.remote.DroneApi

@kotlin.time.ExperimentalTime
class DroneHistoryViewModelFactory(
    private val droneId: String,
    private val api: DroneApi
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DroneHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DroneHistoryViewModel(droneId, api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
