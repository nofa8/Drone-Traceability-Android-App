package dev.epic.dronetraceability.data.remote

import dev.epic.dronetraceability.data.model.Drone
import retrofit2.http.GET
import retrofit2.http.Path

interface DroneApi {

    // Lista todos os drones (GET /api/drones)
    @GET("api/drones")
    suspend fun getSnapshots(): List<Drone>

    // Último snapshot de um drone específico (GET /api/drones/{droneId})
    @GET("api/drones/{droneId}")
    suspend fun getLatestSnapshot(
        @Path("droneId") droneId: String
    ): Drone
}
