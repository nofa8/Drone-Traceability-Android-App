package dev.epic.dronetraceability.data.model.commands

data class StartMissionCommand(
    val startAction: StartAction,
    val endAction: EndAction,
    val repeat: Int? = null,
    val altitude: Double,
    val path: List<GeoPointCommand> = emptyList(),
    val status: MissionStatus
) : DroneCommand(COMMAND) {

    companion object {
        const val COMMAND = "startMission"
        val ALLOWED_COMMANDS = setOf(COMMAND)
    }
}

data class GeoPointCommand(
    val lat: Double,
    val lng: Double
)