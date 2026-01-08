package dev.epic.dronetraceability.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.epic.dronetraceability.ui.commands.DroneCommandViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import dev.epic.dronetraceability.R
import dev.epic.dronetraceability.data.model.commands.FlightCommand


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneCommandContent(
    modifier: Modifier = Modifier,
    viewModel: DroneCommandViewModel
) {
    val drone by viewModel.drone.collectAsState()

    // Se não há drone ainda, mostra loading
    if (drone == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val telemetry = drone!!.telemetry

    // Estado apenas para dropdown
    var flightExpanded by remember { mutableStateOf(false) }
    var selectedFlightCommand by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Flight Commands
        Text("Flight Commands", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { flightExpanded = true },
            enabled = drone!!.isConnected // só ativo se drone online
        ) {
            Text(selectedFlightCommand ?: "Select Flight Command")
        }

        DropdownMenu(
            expanded = flightExpanded,
            onDismissRequest = { flightExpanded = false }
        ) {
            FlightCommand.ALLOWED_COMMANDS.forEach { command ->
                DropdownMenuItem(
                    text = { Text(command) },
                    onClick = {
                        // Atualiza estado do botão
                        selectedFlightCommand = command
                        flightExpanded = false

                        // Envia comando para backend
                        viewModel.sendFlightCommand(command)

                        // Mostra toast
                        Toast.makeText(
                            context,
                            "$command Sent!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Reset do texto para estado inicial
                        selectedFlightCommand = null
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Utility Commands
        Text("Utility Commands", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        // Switches refletem backend diretamente (UI estritamente refletindo backend)
        UtilitySwitch(
            label = "motors",
            checked = telemetry.areMotorsOn,
            onToggle = { viewModel.sendUtilityCommand("motors", it) },
            enabled = drone!!.isConnected
        )

        UtilitySwitch(
            label = "identify",
            checked = telemetry.areLightsOn,
            onToggle = { viewModel.sendUtilityCommand("identify", it) },
            enabled = drone!!.isConnected
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Start Mission
        Button(
            onClick = viewModel::sendStartMission,
            modifier = Modifier.fillMaxWidth(),
            enabled = drone!!.isConnected
        ) {
            Text("Start Mission")
        }

        Spacer(modifier = Modifier.height(24.dp))


        /* --- IMAGEM DO DRONE --- */
        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.drone),
            contentDescription = "Drone Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@Composable
private fun UtilitySwitch(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(
            checked = checked,
            onCheckedChange = { if (enabled) onToggle(it) },
            enabled = enabled
        )
    }
}

