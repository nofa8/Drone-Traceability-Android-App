package dev.epic.dronetraceability.data.repository

import dev.epic.dronetraceability.data.model.domain.Drone
import dev.epic.dronetraceability.data.remote.DroneApi
import dev.epic.dronetraceability.data.remote.DroneWebSocketClient
import dev.epic.dronetraceability.data.model.dtos_ws.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.*

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

    override fun sendFlightCommand(droneId: String, command: String) {
        sendCommand(droneId, "FlightCommand", mapOf("command" to command))
    }

    override  fun sendUtilityCommand(droneId: String, command: String, state: Boolean) {
        sendCommand(droneId, "UtilityCommand", mapOf("command" to command, "state" to state))
    }

    override fun sendStartMissionCommand(
        droneId: String,
        startAction: String,
        endAction: String,
        repeat: Int,
        altitude: Double
    ) {
        sendCommand(
            droneId,
            "StartMissionCommand",
            mapOf(
                "command" to "startMission",
                "startAction" to startAction,
                "endAction" to endAction,
                "repeat" to repeat,
                "altitude" to altitude,
                "path" to emptyList<Map<String, Double>>(),
                "status" to "RUNNING"
            )
        )
    }


    private fun sendCommand(droneId: String, role: String, payload: Map<String, Any?>) {
        // Converte o Map<String, Any?> para JsonObject
        val messageElement = buildJsonObject {
            payload.forEach { (key, value) ->
                when (value) {
                    is String -> put(key, value)
                    is Boolean -> put(key, value)
                    is Number -> put(key, value)
                    is Map<*, *> -> put(key, JsonObject((value as Map<String, Any?>).mapValues {
                        JsonPrimitive(it.value.toString())
                    }))
                    else -> put(key, value.toString())
                }
            }
        }

        val envelope = buildJsonObject {
            put("userId", droneId)
            put("role", role)
            put("message", messageElement)
        }

        wsClient.send(envelope.toString())
    }
}
