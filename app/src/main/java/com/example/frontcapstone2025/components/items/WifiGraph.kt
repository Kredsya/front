package com.example.frontcapstone2025.components.items

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

data class WifiGraphPoint(
    val time: Int,
    val rssi: Double,
    val distance: Double
)

@SuppressLint("DefaultLocale")
@Composable
fun WifiGraph(
    rawPoints: List<WifiGraphPoint>,
    filteredPoints: List<WifiGraphPoint>,
    modifier: Modifier = Modifier
) {
    val currentRawPoints: MutableState<List<WifiGraphPoint>> = remember { androidx.compose.runtime.mutableStateOf(rawPoints) }
    val currentFilteredPoints: MutableState<List<WifiGraphPoint>> = remember { androidx.compose.runtime.mutableStateOf(filteredPoints) }
    LaunchedEffect(rawPoints) {
        currentRawPoints.value = rawPoints
    }
    LaunchedEffect(filteredPoints) {
        currentFilteredPoints.value = filteredPoints
    }
    val allPoints = currentRawPoints.value + currentFilteredPoints.value
    val timeMax = allPoints.maxOfOrNull { it.time } ?: 0
    val rssiMax = allPoints.maxOfOrNull { it.rssi } ?: 0.0
    val rssiMin = allPoints.minOfOrNull { it.rssi } ?: -100.0

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val padding = 16.dp.toPx()
        val width = size.width - padding * 2
        val height = size.height - padding * 2

        // Axes
        drawLine(
            color = Color.Gray,
            start = Offset(padding, padding),
            end = Offset(padding, padding + height),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Gray,
            start = Offset(padding, padding + height),
            end = Offset(padding + width, padding + height),
            strokeWidth = 2f
        )

        val textPaint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 32f
        }

        // Axis labels
        drawContext.canvas.nativeCanvas.apply {
            val xSteps = 4
            for (i in 0..xSteps) {
                val t = (timeMax * i) / xSteps
                val x = padding + (i / xSteps.toFloat()) * width
                drawText(t.toString(), x, padding + height + 32f, textPaint)
            }
            val ySteps = 4
            for (i in 0..ySteps) {
                val value = rssiMax - (rssiMax - rssiMin) * i / ySteps
                val y = padding + (i / ySteps.toFloat()) * height
                drawText(String.format("%.1f", value), padding - 48f, y + 8f, textPaint)
            }
        }

        if (allPoints.isNotEmpty()) {
            val rawPath = Path()
            currentRawPoints.value.forEachIndexed { index, point ->
                val x = padding + (point.time.toFloat() / timeMax.coerceAtLeast(1)) * width
                val y = padding + height - ((point.rssi - rssiMin).toFloat() / (rssiMax - rssiMin).toFloat()) * height
                if (index == 0) rawPath.moveTo(x, y) else rawPath.lineTo(x, y)
                drawCircle(color = Color.Red, radius = 4.dp.toPx(), center = Offset(x, y))
            }
            drawPath(
                path = rawPath,
                color = Color.Red,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            val filteredPath = Path()
            currentFilteredPoints.value.forEachIndexed { index, point ->
                val x = padding + (point.time.toFloat() / timeMax.coerceAtLeast(1)) * width
                val y = padding + height - ((point.rssi - rssiMin).toFloat() / (rssiMax - rssiMin).toFloat()) * height
                if (index == 0) filteredPath.moveTo(x, y) else filteredPath.lineTo(x, y)
                drawCircle(color = Color.Blue, radius = 4.dp.toPx(), center = Offset(x, y))
            }
            drawPath(
                path = filteredPath,
                color = Color.Blue,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}