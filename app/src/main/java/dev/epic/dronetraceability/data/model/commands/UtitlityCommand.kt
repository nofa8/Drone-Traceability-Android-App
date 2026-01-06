package dev.epic.dronetraceability.data.model.commands

class UtilityCommand(
    val state: Boolean,
    command: String
) : DroneCommand(command) {

    companion object {
        val ALLOWED_COMMANDS = setOf(
            "motors",
            "identify",
            "virtualSticks"
        )
    }
}
