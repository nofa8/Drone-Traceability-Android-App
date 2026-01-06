package dev.epic.dronetraceability.data.model.commands

/**
 * Base type for all commands sent to the drone backend.
 *
 * Convention:
 * - Each subclass MUST expose its allowed commands
 *   via a companion object named `ALLOWED_COMMANDS`.
 * - The `command` value MUST be one of them.
 */
sealed class DroneCommand(
    val command: String
)