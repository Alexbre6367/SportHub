package com.example.sporthub.ui.components.face.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.example.sporthub.ui.theme.LightBlue
import com.google.mlkit.vision.face.Face

@Composable
fun FaceDetector(
    face: List<Face>,
    imageWidth: Int,
    imageHeight: Int
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if(face.isEmpty() || imageWidth == 0 || imageHeight == 0) return@Canvas

        val scaleX = size.width / imageWidth
        val scaleY = size.height / imageHeight

        face.forEach { face ->
            face.allContours.forEach { contour ->
                contour.points.forEach { point ->
                    drawCircle(
                        LightBlue,
                        3f,
                        Offset(size.width - (point.x * scaleX), point.y * scaleY)
                    )
                }
            }
        }

    }
}