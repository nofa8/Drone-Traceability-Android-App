package dev.epic.dronetraceability.data.model.domain
import kotlinx.serialization.Serializable

@Serializable
data class Drone(
    val droneId: String,
    val model: String,
    val firstSeenAt: String,
    val isConnected: Boolean,
    val telemetry: Telemetry
)
