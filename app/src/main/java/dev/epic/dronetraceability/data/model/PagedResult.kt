package dev.epic.dronetraceability.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PagedResult<T>(
    val items: List<T>,
    val prevCursor: String?,
    val nextCursor: String?
)