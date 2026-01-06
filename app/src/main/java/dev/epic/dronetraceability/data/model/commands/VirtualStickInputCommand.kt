package dev.epic.dronetraceability.data.model.commands

data class VirtualSticksInputCommand(
    val yaw: Double,
    val pitch: Double,
    val roll: Double,
    val throttle: Double
) : DroneCommand(COMMAND) {

    companion object {
        const val COMMAND = "virtualSticksInput"
        val ALLOWED_COMMANDS = setOf(COMMAND)
    }
}
