package dev.epic.dronetraceability.data.model.dtos_ws

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class EventEnvelope<T>(
    @SerialName("eventType")
    val eventType: EventType,

    @SerialName("timeStamp")
    val timestamp: String,

    @SerialName("payload")
    val payload: T
)

@Serializable
enum class EventType {
    DroneTelemetryReceived,
    DroneDisconnected
}

