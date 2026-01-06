package dev.epic.dronetraceability.data.remote

import dev.epic.dronetraceability.data.model.domain.Drone
import dev.epic.dronetraceability.data.model.PagedResult
import dev.epic.dronetraceability.data.model.domain.Telemetry
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DroneApi {

    // GET /api/drones
    @GET("api/drones")
    suspend fun getDrones(
        @Query("limit") limit: Int = 50,
        @Query("cursor") cursor: String? = null,
        @Query("forward") forward: Boolean = false,
        @Query("isConnected") isConnected: Boolean? = null
    ): PagedResult<Drone>

    // GET /api/drones/{droneId}
    @GET("api/drones/{droneId}")
    suspend fun getDroneById(
        @Path("droneId") droneId: String
    ): Drone

    // GET /api/drones/{droneId}/telemetry
    @GET("api/drones/{droneId}/telemetry")
    suspend fun getTelemetry(
        @Path("droneId") droneId: String,
        @Query("limit") limit: Int = 50,
        @Query("cursor") cursor: String? = null,
        @Query("forward") forward: Boolean = false,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null
    ): PagedResult<Telemetry>
}
