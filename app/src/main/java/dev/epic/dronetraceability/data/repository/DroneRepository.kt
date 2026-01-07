package dev.epic.dronetraceability.data.repository

import dev.epic.dronetraceability.data.model.domain.Drone
import dev.epic.dronetraceability.data.model.domain.Telemetry
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository responsável pela gestão de estado dos drones.
 *
 * Atua como a *Single Source of Truth* para a camada de apresentação,
 * agregando dados provenientes de:
 *
 *  - Endpoints REST (snapshot inicial e consistência de estado)
 *  - WebSocket (atualizações em tempo real)
 *
 * Responsabilidades:
 *  - Expor o estado atual dos drones através de [StateFlow]
 *  - Efetuar o carregamento inicial via REST : (refresh() )
 *  - Iniciar e observar o canal WebSocket : (startRealtime() )
 *  - Aplicar atualizações incrementais ao estado (telemetria, disconnects, etc.)
 *
 * O Repository garante que:
 *  - A UI observa apenas estado reativo, sem conhecer a origem dos dados
 *  - As atualizações são imutáveis e seguras para Jetpack Compose
 */

interface DroneRepository {
    val drones: StateFlow<Map<String, Drone>>
    val isLoading: StateFlow<Boolean>
    val error: StateFlow<String?>
    suspend fun refresh()
    fun startRealtime()
}