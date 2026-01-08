package dev.epic.dronetraceability.ui.commands

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.epic.dronetraceability.data.model.domain.Drone
import dev.epic.dronetraceability.data.repository.DroneRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DroneCommandViewModel(
    private val droneId: String,
    private val repo: DroneRepository
) : ViewModel() {

    val drone: StateFlow<Drone?> =
        repo.drones
            .map { it[droneId] }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    fun sendFlightCommand(command: String) {
        repo.sendFlightCommand(droneId, command)
    }

    fun sendUtilityCommand(command: String, state: Boolean) {
        repo.sendUtilityCommand(droneId, command, state)
    }

    fun sendStartMission() {
        repo.sendStartMissionCommand(
            droneId = droneId,
            startAction = "takeoff",
            endAction = "land",
            repeat = 0,
            altitude = 50.0
        )
    }
}
