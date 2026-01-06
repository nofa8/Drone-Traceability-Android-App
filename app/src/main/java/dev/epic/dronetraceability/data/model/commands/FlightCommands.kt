package dev.epic.dronetraceability.data.model.commands

class FlightCommand(
    command: String
) : DroneCommand(command) {

    companion object {
        val ALLOWED_COMMANDS = setOf(
            "takeoff",
            "land",
            "startGoHome",
            "pauseMission",
            "stopMission",
            "startMission"
        )
    }
}
