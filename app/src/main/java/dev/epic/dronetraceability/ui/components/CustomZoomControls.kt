package dev.epic.dronetraceability.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.launch

@Composable
fun CustomZoomControls(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState
) {
    val scope = rememberCoroutineScope()

    fun zoomBy(amount: Float) {
        scope.launch {
            cameraPositionState.animate(
                update = CameraUpdateFactory.zoomBy(amount),
                durationMs = 300
            )
        }
    }

    Column(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
    ) {
        // Zoom In Button (Plus)
        IconButton(
            onClick = { zoomBy(1f) },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Zoom In", tint = MaterialTheme.colorScheme.primary)
        }

        // Separator
        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

        // Zoom Out Button (Minus)
        IconButton(
            onClick = { zoomBy(-1f) },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Filled.Remove, contentDescription = "Zoom Out", tint = MaterialTheme.colorScheme.primary)
        }
    }
}