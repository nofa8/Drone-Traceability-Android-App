package dev.epic.dronetraceability.data.model

data class Drone(
    val id: Long,
    val lat: Double,
    val lng: Double,
    val homeLocation: HomeLocation?,
    val alt: Double,
    val velX: Double,
    val velY: Double,
    val velZ: Double,
    val batLvl: Int,
    val batTemperature: Double,
    val hdg: Double,
    val satCount: Int,
    val rft: Int,
    val isTraveling: Boolean,
    val isFlying: Boolean,
    val model: String,
    val online: Boolean,
    val isGoingHome: Boolean,
    val isHomeLocationSet: Boolean,
    val areMotorsOn: Boolean,
    val areLightsOn: Boolean
)

data class HomeLocation(
    val lat: Double,
    val lng: Double
)