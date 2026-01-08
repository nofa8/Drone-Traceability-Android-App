package dev.epic.dronetraceability.data.model.commands

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.Serializable
@Serializable
sealed class ExternalEnvelope {
    abstract val userId: String
    abstract val role: String?
    abstract val message: JsonElement
}
