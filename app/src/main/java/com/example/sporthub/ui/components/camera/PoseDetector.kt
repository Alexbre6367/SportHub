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

        fun drawLine(startType: Int, endType: Int) {
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

        drawLine(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER)
        drawLine(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP)
        drawLine(PoseLandmark.RIGHT_HIP, PoseLandmark.LEFT_HIP)
        drawLine(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER)
        drawLine(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW)
        drawLine(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST)
        drawLine(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW)
        drawLine(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST)
        drawLine(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE)
        drawLine(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE)
        drawLine(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE)
        drawLine(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE)
        drawLine(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER)

        fun drawJoint(type: Int) {
            val landmark = pose.getPoseLandmark(type)
            if(landmark != null && landmark.inFrameLikelihood > 0.8f) {
                drawCircle(
                    color = LightBlue,
                    radius = 12f,
                    center = Offset(landmark.position.x * scaleX, landmark.position.y * scaleY)
                )
            }
        }

        drawJoint(PoseLandmark.NOSE)
        drawJoint(PoseLandmark.LEFT_SHOULDER)
        drawJoint(PoseLandmark.RIGHT_SHOULDER)
        drawJoint(PoseLandmark.LEFT_ELBOW)
        drawJoint(PoseLandmark.RIGHT_ELBOW)
        drawJoint(PoseLandmark.LEFT_WRIST)
        drawJoint(PoseLandmark.RIGHT_WRIST)
        drawJoint(PoseLandmark.LEFT_HIP)
        drawJoint(PoseLandmark.RIGHT_HIP)
        drawJoint(PoseLandmark.LEFT_KNEE)
        drawJoint(PoseLandmark.RIGHT_KNEE)
        drawJoint(PoseLandmark.LEFT_ANKLE)
        drawJoint(PoseLandmark.RIGHT_ANKLE)
    }
}