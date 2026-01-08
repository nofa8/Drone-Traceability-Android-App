package dev.epic.dronetraceability.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import dev.epic.dronetraceability.navigation.RepositoryProvider
import dev.epic.dronetraceability.ui.components.CustomZoomControls
import dev.epic.dronetraceability.ui.components.DroneStatusCardMap
import dev.epic.dronetraceability.ui.components.MapTypeSelector
import dev.epic.dronetraceability.ui.components.batteryMarker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneMapScreen(
    navController: NavController,
    droneId: String,
    viewModel: DroneMapViewModel = viewModel(
        factory = DroneMapViewModelFactory(droneId, RepositoryProvider.droneRepository)
    )
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val scope = rememberCoroutineScope()

    var userHasMovedCamera by remember { mutableStateOf(false) }
    var mapType by remember { mutableStateOf(MapType.HYBRID) }

    if(telemetry == null)
        return CircularProgressIndicator()

    val defaultLocation = remember { LatLng(telemetry!!.latitude, telemetry!!.longitude) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 17f)
    }

    var mapLoaded by remember { mutableStateOf(false) }

    // Lista de posições do rastro do drone (últimos 150 pontos)
    val pathPoints = remember { mutableStateListOf<LatLng>() }

    // Inicializa o rastro com a primeira posição do drone
    LaunchedEffect(telemetry) {
        telemetry?.let {
            val pos = LatLng(it.latitude, it.longitude)
            if (pathPoints.isEmpty()) {
                pathPoints.add(pos)
            }
        }
    }

    // Adiciona pontos ao rastro a cada 5 segundos
    LaunchedEffect(telemetry) {
        while (true) {
            telemetry?.let {
                val pos = LatLng(it.latitude, it.longitude)
                if (pathPoints.lastOrNull() != pos) {
                    pathPoints.add(pos)
                    if (pathPoints.size > 150) pathPoints.removeAt(0)
                }
            }
            kotlinx.coroutines.delay(5000)
        }
    }

    // Auto-center do drone
    LaunchedEffect(telemetry, mapLoaded) {
        if (!mapLoaded || userHasMovedCamera) return@LaunchedEffect
        telemetry?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)),
                durationMs = 700
            )
        }
    }

    // Detecta gesture do utilizador
    LaunchedEffect(Unit) {
        snapshotFlow { cameraPositionState.isMoving }
            .collect { moving ->
                if (
                    moving &&
                    cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE
                ) {
                    userHasMovedCamera = true
                }
            }
    }

    val recenterCamera: (LatLng, Float) -> Unit = { target, zoom ->
        scope.launch {
            userHasMovedCamera = false
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(target, zoom),
                durationMs = 800
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Drone $droneId") },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // Mensagens de erro ou loading
            when {
                error != null -> {
                    Text(
                        text = "Erro: $error",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                isLoading && telemetry == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // Google Map
            telemetry?.let { t ->
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(mapType = mapType),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        compassEnabled = true,
                        mapToolbarEnabled = false
                    ),
                    onMapLoaded = { mapLoaded = true }
                ) {
                    val dronePos = LatLng(t.latitude, t.longitude)
                    val icon = remember(t.batteryLevel) { batteryMarker(t.batteryLevel) }

                    // Marker atual do drone
                    Marker(
                        state = rememberUpdatedMarkerState(position = dronePos),
                        title = "Drone $droneId",
                        snippet = "Altitude: ${"%.1f".format(t.altitude)} m • Battery: ${t.batteryLevel}%",
                        icon = icon
                    )

                    // Polyline mostrando o rastro
                    if (pathPoints.size > 1) {
                        Polyline(
                            points = pathPoints.toList(), // converte para List
                            color = androidx.compose.ui.graphics.Color.Blue,
                            width = 15f
                        )
                    }
                }

                // Drone status card
                DroneStatusCardMap(
                    droneId = droneId,
                    telemetry = t,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }

            // Map type selector
            MapTypeSelector(
                currentType = mapType,
                onTypeSelected = { mapType = it },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            // Recenter button
            FloatingActionButton(
                onClick = {
                    telemetry?.let {
                        recenterCamera(LatLng(it.latitude, it.longitude), 16f)
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Filled.LocationSearching,
                    contentDescription = "Recenter on Drone"
                )
            }

            // Zoom controls
            CustomZoomControls(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 160.dp)
                    .width(40.dp),
                cameraPositionState = cameraPositionState
            )
        }
    }
}







