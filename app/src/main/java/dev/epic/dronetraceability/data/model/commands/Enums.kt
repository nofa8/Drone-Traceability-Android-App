package dev.epic.dronetraceability.data.model.commands

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StartAction {
    @SerialName("takeoff") Takeoff,
    @SerialName("none") None
}

@Serializable
enum class EndAction {
    @SerialName("land") Land,
    @SerialName("goHome") GoHome,
    @SerialName("none") None
}

@Serializable
enum class MissionStatus {
    @SerialName("RUNNING") Running,
    @SerialName("PAUSED") Paused,
    @SerialName("COMPLETED") Completed,
    @SerialName("STOPPED") Stopped
}