package dev.epic.dronetraceability.data.model.dtos_ws

import dev.epic.dronetraceability.data.model.domain.Telemetry
import dev.epic.dronetraceability.data.model.domain.GeoPoint

fun TelemetryWsDto.toDomain(): Telemetry =
    Telemetry(
        timestamp = timestamp,
        homeLocation = homeLocation?.toDomain(),
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        velocityX = velocityX,
        velocityY = velocityY,
        velocityZ = velocityZ,
        batteryLevel = batteryLevel,
        batteryTemperature = batteryTemperature,
        heading = heading,
        satelliteCount = satelliteCount,
        remainingFlightTime = remainingFlightTime,
        isTraveling = isTraveling,
        isFlying = isFlying,
        online = online,
        isGoingHome = isGoingHome,
        isHomeLocationSet = isHomeLocationSet,
        areMotorsOn = areMotorsOn,
        areLightsOn = areLightsOn
    )


fun GeoPointWsDto.toDomain(): GeoPoint =
    GeoPoint(latitude, longitude)
