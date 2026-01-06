package dev.epic.dronetraceability.navigation

import dev.epic.dronetraceability.data.api.ApiClient
import dev.epic.dronetraceability.data.remote.DroneWebSocketClient
import dev.epic.dronetraceability.data.repository.DroneRepository
import dev.epic.dronetraceability.data.repository.DroneRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient


object RepositoryProvider {
    val droneRepository: DroneRepository by lazy {
        DroneRepositoryImpl(
            api = ApiClient.api,
            wsClient = DroneWebSocketClient(OkHttpClient()),
            json = Json { ignoreUnknownKeys = true },
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        )
    }
}
