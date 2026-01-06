package dev.epic.dronetraceability.data.model.dtos_ws

import kotlinx.serialization.Serializable

@Serializable
data class GeoPointWsDto(
    val latitude: Double,
    val longitude: Double
)