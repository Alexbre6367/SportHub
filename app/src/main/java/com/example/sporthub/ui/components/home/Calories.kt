package com.example.sporthub.ui.components.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.activityRedEnd
import com.example.sporthub.ui.theme.activityRedStart
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

@Composable
fun Calories(
    homeViewModel: HomeViewModel
) {
    val calories by homeViewModel.calories.collectAsState()
    val caloriesGoal = 300f
    val caloriesStrike by homeViewModel.caloriesStrike.collectAsState()
    val currentProgress = (calories.inKilocalories.toFloat() / caloriesGoal).coerceIn(0f, 1f)

    val weekProgress by homeViewModel.week.collectAsState()
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .background(
                color = Color.White, shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 24.dp)
            .padding(start = 14.dp, end = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(120.dp)
                .background(color = Color.White, shape = RoundedCornerShape(24.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(72.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        val brush = Brush.verticalGradient(
                            colors = listOf(activityRedStart, activityRedEnd)
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = caloriesStrike.toString(),
                fontSize = 24.sp,
                color = black,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Calories streak",
                fontSize = 14.sp,
                color = LightGray,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 6.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "${calories.inKilocalories.toInt()}",
                    fontSize = 32.sp,
                    color = black,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = "/300",
                    fontSize = 20.sp,
                    color = black,
                    modifier = Modifier.alignByBaseline()
                )
            }
            Spacer(Modifier.height(8.dp))
            CaloriesBar(
                progress = currentProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                homeViewModel = homeViewModel
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                days.forEachIndexed { index, dayLetter ->
                    val isDayActive = weekProgress.getOrElse(index) { false }
                    Week(
                        days = dayLetter,
                        isActive = isDayActive,
                    )
                }
            }
        }
    }
}

@Composable
fun CaloriesBar(
    progress: Float,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    var animationStart by remember { mutableStateOf(homeViewModel.firstLaunchAnimationCalories) }
    LaunchedEffect(Unit) {
        if(!homeViewModel.firstLaunchAnimationCalories)
            animationStart = true
        homeViewModel.firstLaunchAnimationCalories = true
    }

    val animationCalories by animateFloatAsState(
        targetValue = if(animationStart) progress else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )

    Box(
        modifier = modifier
            .drawBackdrop(
                backdrop = rememberLayerBackdrop(),
                shape = { CircleShape },
                effects = {
                    vibrancy()
                    blur(2f.dp.toPx())
                    lens(16f.dp.toPx(), 32f.dp.toPx())
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animationCalories.coerceIn(0f, 1f))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(activityRedEnd, activityRedStart)
                    ),
                    shape = CircleShape
                )
        )
    }
}

