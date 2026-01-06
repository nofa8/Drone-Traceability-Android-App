package dev.epic.dronetraceability.ui.components
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt

fun batteryMarker(batLvl: Double): BitmapDescriptor {
    val sizePx = 64
    val bitmap = createBitmap(sizePx, sizePx)
    val canvas = Canvas(bitmap)

    val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
    }

    val circlePaint = Paint().apply {
        isAntiAlias = true
        color = when {
            batLvl > 70 -> "#4CAF50".toColorInt() // Green
            batLvl > 30 -> "#FFC107".toColorInt() // Yellow
            else -> "#F44336".toColorInt()        // Red
        }
    }

    val cx = sizePx / 2f
    val cy = sizePx / 2f
    val outerRadius = sizePx / 2f
    val innerRadius = sizePx / 2.6f

    // Outer white circle
    canvas.drawCircle(cx, cy, outerRadius, backgroundPaint)
    // Inner colored circle
    canvas.drawCircle(cx, cy, innerRadius, circlePaint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}