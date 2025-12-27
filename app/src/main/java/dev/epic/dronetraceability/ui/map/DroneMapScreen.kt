package dev.epic.dronetraceability.ui.map


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationSearching
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
import dev.epic.dronetraceability.ui.dronedetail.DroneDetailViewModel
import dev.epic.dronetraceability.ui.dronedetail.DroneDetailViewModelFactory
import kotlinx.coroutines.launch
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import dev.epic.dronetraceability.ui.components.CustomZoomControls
import dev.epic.dronetraceability.ui.components.DroneStatusCard
import dev.epic.dronetraceability.ui.components.MapTypeSelector
import dev.epic.dronetraceability.ui.components.batteryMarker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneMapScreen(navController: NavController, droneId: Long) {
    val viewModel: DroneDetailViewModel = viewModel(factory = DroneDetailViewModelFactory(droneId))
    val drone by viewModel.drone.collectAsState()
    val scope = rememberCoroutineScope()
    //val context = LocalContext.current
    var userHasMovedCamera by remember { mutableStateOf(false) }
    // Map type state
    var mapType by remember { mutableStateOf(MapType.HYBRID) }
    // camera state with safe default - not recreated on recomposition
    val defaultLocation = remember { LatLng(39.93326, -8.89305) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 14f)
    }

    // Track map readiness and user interaction
    var mapLoaded by remember { mutableStateOf(false) }
    var userMovedMap by remember { mutableStateOf(false) }



    // Auto-center effect: only when map is loaded and user hasn't moved map
    LaunchedEffect(drone?.lat, drone?.lng, mapLoaded, userMovedMap) {
        if (!mapLoaded) return@LaunchedEffect
        val d = drone ?: return@LaunchedEffect
        if (userMovedMap) return@LaunchedEffect

        val target = LatLng(d.lat, d.lng)
        if (userHasMovedCamera) {
            return@LaunchedEffect
        }
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLng(target),
            durationMs = 700
        )
    }

    // Recenter helper
    val recenterCamera: (LatLng, Float) -> Unit = { target, zoom ->
        scope.launch {
            userHasMovedCamera = false // reset after programmatic recenter
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(target, zoom),
                durationMs = 800
            )

        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { cameraPositionState.position.target }.collect {
            // Once the camera target changes via a gesture, set the flag.
            if (cameraPositionState.isMoving && cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                userHasMovedCamera = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(drone?.model ?: "Live Map") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
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
                drone?.let { d ->
                    val dronePos = LatLng(d.lat, d.lng)

                    val icon = remember(d.batLvl) { batteryMarker(d.batLvl) }

                    Marker(
                        state = rememberUpdatedMarkerState(position = dronePos),
                        title = d.model,
                        snippet = "Altitude: ${"%.1f".format(d.alt)} m • Battery: ${d.batLvl}%",
                        icon = icon
                    )

                    // Draw polyline for trajectory here if you have points
                    // Polyline(points = ...)
                }
            }

            // Map type picker
            MapTypeSelector(
                currentType = mapType,
                onTypeSelected = { mapType = it },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            // Recenter FAB
            FloatingActionButton(
                onClick = {
                    drone?.let { recenterCamera(LatLng(it.lat, it.lng), 16f) }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.LocationSearching, contentDescription = "Recenter on Drone")
            }

            // Zoom controls
            CustomZoomControls(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 160.dp).width(40.dp),
                cameraPositionState = cameraPositionState
            )

            // Drone status card (bottom center) with animated battery progress
            drone?.let {
                DroneStatusCard(
                    drone = it,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}


