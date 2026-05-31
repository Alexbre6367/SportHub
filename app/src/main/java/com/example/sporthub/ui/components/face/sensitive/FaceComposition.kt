package com.example.sporthub.ui.components.face.sensitive

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.LightWhite
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.appleBlue
import com.example.sporthub.ui.theme.appleOrange
import com.example.sporthub.ui.theme.applePink
import com.example.sporthub.ui.theme.applePurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceComposition(
    sliderPosition: Float,
    onSliderPosition: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(OffWhite, shape = RoundedCornerShape(32.dp))
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FaceComposition",
            color = LightGray,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(20.dp))
        FaceCanvas()

        Spacer(Modifier.height(20.dp))
        Text(
            text = "Tap the areas where your skin reacts most",
            color = LightGray,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(appleBlue, applePurple, applePink, appleOrange)
                        ),
                        shape = CircleShape
                    )
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .background(OffWhite, CircleShape)
                            .size(12.dp)
                    )
                }
            }

            Slider(
                value = sliderPosition,
                onValueChange = onSliderPosition,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 3.dp),
                colors = SliderDefaults.colors(
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                ambientColor = Color.Black.copy(alpha = 0.1f),
                                spotColor = Color.Black.copy(alpha = 0.3f)
                            )
                            .background(OffWhite, CircleShape),
                    )
                },
                valueRange = 0f..60f
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mild",
                color = LightGray,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 14.sp
            )

            Text(
                text = "Sensitive",
                color = LightGray,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 14.sp
            )

            Text(
                text = "Very Sensitive",
                color = LightGray,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 14.sp
            )
        }
    }

    Spacer(Modifier.height(100.dp))
}



@Composable
fun FaceCanvas() {
    Canvas(Modifier.size(300.dp)) {
        val strokeWidth = 8f
        val radius = size.minDimension / 2.5f
        val centerX = size.width / 2
        val centerY = size.height / 2

        drawCircle(
            color = LightWhite,
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = strokeWidth)
        )

        val eyeOffset = radius * 0.4f
        val eyeY = centerY - radius * 0.2f
        drawCircle(
            color = LightWhite,
            radius = radius * 0.08f,
            center = Offset(centerX - eyeOffset, eyeY)
        )

        drawCircle(
            color = LightWhite,
            radius = radius * 0.08f,
            center = Offset(centerX + eyeOffset, eyeY)
        )

        val nosePath = Path().apply {
            moveTo(centerX, centerY - radius * 0.05f)
            lineTo(centerX, centerY + radius * 0.15f)
            lineTo(centerX - radius * 0.12f, centerY + radius * 0.15f)
        }
        drawPath(
            path = nosePath,
            color = LightWhite,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        drawArc(
            color = LightWhite,
            startAngle = 40f,
            sweepAngle = 160f,
            useCenter = false,
            topLeft = Offset(centerX - radius * 0.3f, centerY + radius * 0.15f),
            size = Size(radius * 0.5f, radius * 0.35f),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}