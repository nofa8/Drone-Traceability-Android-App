package dev.epic.dronetraceability.data.model.domain

import kotlinx.serialization.Serializable

@Serializable
data class Telemetry(

    val timestamp: String, // ISO-8601 vindo do backend , mais tarde usar Instant.parse(timestamp)

    val homeLocation: GeoPoint?,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,

    val velocityX: Double,
    val velocityY: Double,
    val velocityZ: Double,

    val batteryLevel: Double,
    val batteryTemperature: Double,

    val heading: Double,
    val satelliteCount: Int,
    val remainingFlightTime: Int,

    val isTraveling: Boolean,
    val isFlying: Boolean,
    val online: Boolean,
    val isGoingHome: Boolean,
    val isHomeLocationSet: Boolean,
    val areMotorsOn: Boolean,
    val areLightsOn: Boolean
)
