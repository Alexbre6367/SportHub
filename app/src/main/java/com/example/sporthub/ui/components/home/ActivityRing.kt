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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.theme.activityBlueEnd
import com.example.sporthub.ui.theme.activityBlueStart
import com.example.sporthub.ui.theme.activityGreenEnd
import com.example.sporthub.ui.theme.activityGreenStart
import com.example.sporthub.ui.theme.activityRedEnd
import com.example.sporthub.ui.theme.activityRedStart
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.himanshoe.charty.circle.CircleChart
import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.common.asGradientChartColor


@Composable
fun ActivityRing(
    homeViewModel: HomeViewModel
) {
    val step by homeViewModel.steps.collectAsState()
    val sleep by homeViewModel.formatSleep.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
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
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Activity Rings",
            color = black,
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(0.5f),
                contentAlignment = Alignment.CenterStart,
            ) { CircleChart(homeViewModel) }

            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Move",
                    color = black,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "$step/10000",
                    color = activityRedStart,
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Sleep",
                    color = black,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "$sleep/8:00",
                    color = activityGreenStart,
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Kilometers",
                    color = black,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${step / 1000}/12",
                    color = activityBlueStart,
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
@Composable
fun CircleChart(
    homeViewModel: HomeViewModel
) {
    val stepGoal = 10000f
    val sleepGoal = 480f
    val kilometers = 12

    val steps by homeViewModel.steps.collectAsState()
    val sleep by homeViewModel.sleep.collectAsState()

    var animationStart by remember { mutableStateOf(homeViewModel.firstLaunchAnimationCircle) }
    LaunchedEffect(Unit) {
        if(!homeViewModel.firstLaunchAnimationCircle) {
            animationStart = true
            homeViewModel.firstLaunchAnimationCircle = true
        }
    }

    val targetSteps = ((steps.toFloat() / stepGoal) * 100f).coerceAtMost(100f)
    val targetSleep = ((sleep.toFloat() / sleepGoal) * 100f).coerceAtMost(100f)
    val targetDistance = (((steps.toFloat() / 1000f) / kilometers) * 100f).coerceAtMost(100f)

    val animationSteps by animateFloatAsState(
        targetValue = if(animationStart) targetSteps else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )

    val animationSleep by animateFloatAsState(
        targetValue = if(animationStart) targetSleep else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )

    val animationDistance by animateFloatAsState(
        targetValue = if(animationStart) targetDistance else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )

    val chartItems = listOf(
        CircleData(
            value = animationSteps,
            label = "",
            color = listOf(activityRedStart, activityRedEnd, activityRedEnd).asGradientChartColor()
        ),
        CircleData(
            value = animationSleep,
            label = "",
            color = listOf(activityGreenStart, activityGreenEnd, activityGreenEnd).asGradientChartColor()
        ),
        CircleData(
            value = animationDistance,
            label = "",
            color = listOf(activityBlueStart, activityBlueEnd, activityBlueEnd).asGradientChartColor()
        )
    )

    CircleChart(
        data = { chartItems },
        modifier = Modifier
            .size(155.dp)
            .graphicsLayer(rotationZ = -180f)
    )
}