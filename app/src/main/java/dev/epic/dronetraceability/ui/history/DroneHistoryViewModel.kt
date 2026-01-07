package dev.epic.dronetraceability.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.epic.dronetraceability.data.remote.DroneApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant


@kotlin.time.ExperimentalTime
class DroneHistoryViewModel(
    private val droneId: String,
    private val api: DroneApi
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    private val pageSize = 10  // items por página

    init {
        loadInitial()
    }

    /** Define os filtros de data e recarrega a primeira página */
    fun setFilters(from: Instant?, to: Instant?) {
        _state.update {
            it.copy(from = from, to = to)
        }
        loadInitial()
    }

    /** Carrega a primeira página, considerando filtros */
    fun loadInitial() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val current = _state.value
                val page = api.getTelemetry(
                    droneId = droneId,
                    from = current.from?.toString(),
                    to = current.to?.toString(),
                    limit = pageSize,
                    forward = false
                )
                _state.update {
                    it.copy(
                        items = page.items,          // substitui items existentes
                        nextCursor = page.nextCursor,
                        prevCursor = page.prevCursor,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    /** Carrega a página anterior */
    fun loadOlder() = loadPage(forward = false, cursor = _state.value.prevCursor)

    /** Carrega a página seguinte */
    fun loadNewer() = loadPage(forward = true, cursor = _state.value.nextCursor)

    /** Função genérica para carregar páginas a partir de cursor */
    private fun loadPage(forward: Boolean, cursor: String?) {
        if (cursor == null) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val current = _state.value
                val page = api.getTelemetry(
                    droneId = droneId,
                    cursor = cursor,
                    forward = forward,
                    from = current.from?.toString(),
                    to = current.to?.toString(),
                    limit = pageSize
                )
                _state.update {
                    it.copy(
                        items = page.items.ifEmpty { it.items },           // substitui items existentes
                        nextCursor = page.nextCursor,
                        prevCursor = page.prevCursor,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}





