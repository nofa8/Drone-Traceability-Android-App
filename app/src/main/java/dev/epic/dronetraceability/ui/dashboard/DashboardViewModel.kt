package dev.epic.dronetraceability.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.epic.dronetraceability.data.repository.DroneRepository
import kotlinx.coroutines.launch

/**
 * ViewModel da Dashboard de drones.
 *
 * Responsável por:
 *  - Expor o estado observado do [DroneRepository] à UI
 *  - Coordenar o carregamento inicial (REST)
 *  - Iniciar o fluxo de atualizações em tempo real (WebSocket)
 *
 * Este ViewModel:
 *  - Não contém lógica de negócio
 *  - Não faz parsing de dados
 *  - Não conhece detalhes de REST ou WebSocket
 *
 * Atua apenas como um *state manager* para Jetpack Compose.
 */

class DashboardViewModel(
    private val repo: DroneRepository
) : ViewModel() {

    val drones = repo.drones
    val isLoading = repo.isLoading

    val error = repo.error

    init {
        viewModelScope.launch {
            repo.refresh()
            repo.startRealtime()
        }
    }
}
