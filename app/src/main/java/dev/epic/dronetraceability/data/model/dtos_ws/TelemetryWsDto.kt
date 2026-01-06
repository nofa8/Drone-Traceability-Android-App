package dev.epic.dronetraceability.data.model.dtos_ws

import kotlinx.serialization.Serializable
@Serializable
data class TelemetryWsDto(
    val timestamp: String,
    val homeLocation: GeoPointWsDto?,
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
