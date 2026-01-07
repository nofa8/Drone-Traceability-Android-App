package dev.epic.dronetraceability.ui.history

import dev.epic.dronetraceability.data.model.domain.Telemetry

import java.time.Instant

data class HistoryUiState(
    val items: List<Telemetry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val nextCursor: String? = null,
    val prevCursor: String? = null,
    val from: Instant? = null,
    val to: Instant? = null
)
