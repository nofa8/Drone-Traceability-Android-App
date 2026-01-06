package dev.epic.dronetraceability.data.model.domain

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    val latitude: Double,
    val longitude: Double
)
