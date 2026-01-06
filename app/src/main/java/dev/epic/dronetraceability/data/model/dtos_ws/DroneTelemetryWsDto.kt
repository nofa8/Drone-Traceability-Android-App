package dev.epic.dronetraceability.data.model.dtos_ws

import kotlinx.serialization.Serializable

@Serializable
data class DroneTelemetryWsDto(
    val droneId: String,
    val model: String,
    val telemetry: TelemetryWsDto
)
