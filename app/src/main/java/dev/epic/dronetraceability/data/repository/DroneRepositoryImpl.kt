package dev.epic.dronetraceability.data.repository

import dev.epic.dronetraceability.data.model.domain.Drone
import dev.epic.dronetraceability.data.model.domain.Telemetry
import dev.epic.dronetraceability.data.remote.DroneApi
import dev.epic.dronetraceability.data.remote.DroneWebSocketClient
import dev.epic.dronetraceability.data.model.dtos_ws.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.builtins.serializer

class DroneRepositoryImpl(
    private val api: DroneApi,
    private val wsClient: DroneWebSocketClient,
    private val json: Json,
    private val scope: CoroutineScope
) : DroneRepository {

    private val _drones = MutableStateFlow<Map<String, Drone>>(emptyMap())
    override val drones: StateFlow<Map<String, Drone>> = _drones

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    override val error: StateFlow<String?> = _error

    override suspend fun refresh() {
        try {
            _isLoading.value = true
            _error.value = null

            val snapshot = api.getDrones().items
            _drones.value = snapshot.associateBy { it.droneId }

        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _isLoading.value = false
        }
    }

    override fun startRealtime() {
        wsClient.connect("ws://10.0.2.2:5102")
        observeWebSocket()
    }

    private fun observeWebSocket() {
        scope.launch {
            wsClient.events.collect { raw ->
                val root = json.parseToJsonElement(raw).jsonObject
                val type = root["eventType"]!!.jsonPrimitive.content

                when (type) {
                    "DroneTelemetryReceived" ->
                        handleTelemetry(
                            json.decodeFromJsonElement(
                                EventEnvelope.serializer(DroneTelemetryWsDto.serializer()),
                                root
                            )
                        )

                    "DroneDisconnected" ->
                        handleDisconnected(
                            json.decodeFromJsonElement(
                                EventEnvelope.serializer(String.serializer()),
                                root
                            )
                        )
                }
            }
        }
    }

    private fun handleTelemetry(
        event: EventEnvelope<DroneTelemetryWsDto>
    ) {
        val ws = event.payload
        val telemetry = ws.telemetry.toDomain()

        _drones.update { current ->
            val existing = current[ws.droneId]

            val drone =
                if (existing == null) {
                    Drone(
                        droneId = ws.droneId,
                        model = ws.model,
                        firstSeenAt = event.timestamp,
                        isConnected = true,
                        telemetry = telemetry
                    )
                } else {
                    existing.copy(
                        telemetry = telemetry,
                        isConnected = true
                    )
                }

            current + (drone.droneId to drone)
        }
    }

    private fun handleDisconnected(
        event: EventEnvelope<String>
    ) {
        _drones.update { current ->
            val drone = current[event.payload] ?: return@update current
            current + (drone.droneId to drone.copy(isConnected = false))
        }
    }
}
