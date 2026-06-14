package com.example.sporthub.ui.components.camera

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.gray
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseDetector(
    pose: Pose,
    imageWidth: Int,
    imageHeight: Int
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val landmarks = pose.allPoseLandmarks
        if(landmarks.isEmpty()) return@Canvas

        val scaleX = size.width / imageWidth
        val scaleY = size.height / imageHeight

        fun drawSkeleton(startType: Int, endType: Int) {
            val start = pose.getPoseLandmark(startType)
            val end = pose.getPoseLandmark(endType)
            if(start != null && end != null && start.inFrameLikelihood > 0.8f && end.inFrameLikelihood > 0.5f) {
                drawLine(
                    color = gray,
                    start = Offset(start.position.x * scaleX, start.position.y * scaleY),
                    end = Offset(end.position.x * scaleX, end.position.y * scaleY),
                    strokeWidth = 4f
                )
            }
        }

        drawSkeleton(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER)
        drawSkeleton(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP)
        drawSkeleton(PoseLandmark.RIGHT_HIP, PoseLandmark.LEFT_HIP)
        drawSkeleton(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER)
        drawSkeleton(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW)
        drawSkeleton(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST)
        drawSkeleton(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW)
        drawSkeleton(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST)
        drawSkeleton(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE)
        drawSkeleton(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE)
        drawSkeleton(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE)
        drawSkeleton(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE)
        drawSkeleton(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER)

        fun drawFace(type: Int) {
            val landmark = pose.getPoseLandmark(type)
            if(landmark != null && landmark.inFrameLikelihood > 0.8f) {
                drawCircle(
                    color = LightBlue,
                    radius = 12f,
                    center = Offset(landmark.position.x * scaleX, landmark.position.y * scaleY)
                )
            }
        }

        drawFace(PoseLandmark.NOSE)
        drawFace(PoseLandmark.LEFT_SHOULDER)
        drawFace(PoseLandmark.RIGHT_SHOULDER)
        drawFace(PoseLandmark.LEFT_ELBOW)
        drawFace(PoseLandmark.RIGHT_ELBOW)
        drawFace(PoseLandmark.LEFT_WRIST)
        drawFace(PoseLandmark.RIGHT_WRIST)
        drawFace(PoseLandmark.LEFT_HIP)
        drawFace(PoseLandmark.RIGHT_HIP)
        drawFace(PoseLandmark.LEFT_KNEE)
        drawFace(PoseLandmark.RIGHT_KNEE)
        drawFace(PoseLandmark.LEFT_ANKLE)
        drawFace(PoseLandmark.RIGHT_ANKLE)
    }
}