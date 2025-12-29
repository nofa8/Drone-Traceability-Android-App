package dev.epic.dronetraceability.data.repository

import dev.epic.dronetraceability.data.model.Drone
import dev.epic.dronetraceability.data.remote.DroneApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DroneRepositoryBackend(
    private val api: DroneApi
) {

    /**
     * Buscar lista de drones
     */
    suspend fun getDrones(): Result<List<Drone>> =
        safeCall {
            api.getSnapshots()  // Chama o endpoint GET /api/drones
        }

    /**
     * Buscar último snapshot de um drone específico
     */
    suspend fun getDroneById(droneId: String): Result<Drone> =
        safeCall {
            api.getLatestSnapshot(droneId) // GET /api/drones/{droneId}
        }

    /**
     * Wrapper para tratar exceções e correr no IO Dispatcher
     */
    private suspend fun <T> safeCall(
        block: suspend () -> T
    ): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(block())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
