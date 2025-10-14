package dev.epic.dronetraceability.data.repository

import dev.epic.dronetraceability.data.model.Drone
import dev.epic.dronetraceability.data.model.HomeLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DroneRepository {
    fun getDrones(): Flow<List<Drone>> = flow {
        val longid = 1L
        while (true) {
            emit(
                listOf(
                    randomDrone(longid),
                    randomDroneWorking(longid+1),
                    randomDrone(longid+2),
                    randomDrone(longid+3),
                    randomDrone(longid+4),
                    randomDrone(longid+5),
                )
            )
            delay(1000)
        }
    }
    private fun randomDrone(id: Long): Drone = Drone(
        id = id,
        lat = 39.93326,
        lng = -8.89305,
        homeLocation = HomeLocation(39.933219, -8.892509),
        alt = (0..120).random().toDouble(),
        velX = (0..5).random().toDouble(),
        velY = (0..5).random().toDouble(),
        velZ = (0..5).random().toDouble(),
        batLvl = (50..100).random(),
        batTemperature = (25..35).random().toDouble(),
        hdg = (0..360).random().toDouble(),
        satCount = (5..15).random(),
        rft = (0..10).random(),
        isTraveling = listOf(true, false).random(),
        isFlying = listOf(true, false).random(),
        model = "Mavic 2 Enterprise Advanced",
        online = true,
        isGoingHome = listOf(true, false).random(),
        isHomeLocationSet = true,
        areMotorsOn = listOf(true, false).random(),
        areLightsOn = listOf(true, false).random()
    )

    private fun randomDroneWorking(id: Long): Drone {
        // Generate slight offsets for latitude and longitude
        // Random value between -0.025 and 0.025
        val latOffset = (Math.random() - 0.25) * 0.0005
        val lngOffset = (Math.random() - 0.25) * 0.0005

        val baseLat = 39.93326
        val baseLng = -8.89305
        val homeLat = 39.933219
        val homeLng = -8.892509

        return Drone(
            id = id,
            lat = baseLat + latOffset,
            lng = baseLng + lngOffset,
            homeLocation = HomeLocation(homeLat, homeLng),
            alt = (0..120).random().toDouble(),
            velX = (0..5).random().toDouble(),
            velY = (0..5).random().toDouble(),
            velZ = (0..5).random().toDouble(),
            batLvl = (50..100).random(),
            batTemperature = (25..35).random().toDouble(),
            hdg = (0..360).random().toDouble(),
            satCount = (5..15).random(),
            rft = (0..10).random(),
            isTraveling = true,
            isFlying = true,
            model = "Mavic 2 Enterprise Advanced",
            online = true,
            isGoingHome = false,
            isHomeLocationSet = true,
            areMotorsOn = true,
            areLightsOn = listOf(true, false).random()
        )
    }

}