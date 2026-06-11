package com.example.sporthub.ui.components.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.components.baseGlass
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlin.math.sin

@Composable
fun WaterButton(
    waterCount: Int,
    onAddClick: () -> Unit
) {
    val maxWater = 10f
    val targetProgress = (waterCount.toFloat() / maxWater).coerceIn(0f, 1f)
    val animatedProgress = animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "Уровень воды"
    )

    val isAnimating = animatedProgress.value > 0.01f && animatedProgress.value < 0.99f
    val infiniteTransition = rememberInfiniteTransition(label = "wave")

    val wavePhase by if(isAnimating) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 2f * Math.PI.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Волны"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .baseGlass(rememberLayerBackdrop(), effects = false)
            .clip(CircleShape)
            .clickable(
                onClick = { onAddClick() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val waterLevelY = height * (1f - animatedProgress.value)

            if(animatedProgress.value == 1f) {
                drawCircle(
                    color = LightBlue,
                    radius = size.minDimension / 2
                )
            } else if(animatedProgress.value > 0.01f) {
                val wavesPath = Path().apply {
                    moveTo(0f, waterLevelY)
                    val waveHeight = 4.dp.toPx()

                    for (x in 0..width.toInt() step 5) {
                        val relativeX = x / width
                        val y = waterLevelY + (waveHeight * sin(relativeX * 2 * Math.PI + wavePhase)).toFloat()
                        lineTo(x.toFloat(), y)
                    }
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(
                    path = wavesPath,
                    color = LightBlue
                )
            }
        }
    }
}

@Composable
fun WaterTarget(
    homeViewModel: HomeViewModel
) {
    val water by homeViewModel.water.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .background(
                color = Color.White, shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Daily Drink Target",
                color = black,
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text =
                    if (water >= 10)
                        "Daily goal reached!"
                    else
                        "${water * 200}ml water (${water} Glasses)",
                color = black,
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.weight(1f))
            Row(
                Modifier
                    .fillMaxHeight()
                    .clickable(
                        onClick = { homeViewModel.addWater() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .baseGlass(rememberLayerBackdrop(), effects = false)
                        .height(40.dp)
                        .width(130.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Drink 200 ml",
                        color = black,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .baseGlass(rememberLayerBackdrop(), effects = false)
                        .size(40.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.WineBar,
                        contentDescription = null,
                        tint = black,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd
        ) {
            WaterButton(
                waterCount = water,
                onAddClick = { homeViewModel.addWater() }
            )
        }
    }
}