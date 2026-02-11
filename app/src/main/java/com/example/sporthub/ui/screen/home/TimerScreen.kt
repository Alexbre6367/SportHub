package com.example.sporthub.ui.screen.home

import android.graphics.PathMeasure
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.theme.ringColor
import com.example.sporthub.ui.viewmodel.TimerViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel
) {
    val scrollState = rememberScrollState()
    val secondsLeft by timerViewModel.timerLeft.collectAsStateWithLifecycle()
    val totalSeconds by timerViewModel.totalSeconds.collectAsStateWithLifecycle()

    var selectedTime by remember { mutableIntStateOf(0) }

    var selectedMinutes by remember { mutableIntStateOf(0) }
    var selectedSeconds by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    if (secondsLeft <= 0L) {
        selectedTime = 0
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite), startY = 0f, endY = 1500f
                )
            )
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(70.dp))
        TimerRing(
            secondsLeft = secondsLeft,
            totalSeconds = totalSeconds,
            modifier = Modifier
                .drawBackdrop(
                    backdrop = rememberLayerBackdrop(),
                    shape = { RoundedCornerShape(72.dp) },
                    effects = {
                        vibrancy()
                        blur(2f.dp.toPx())
                        lens(16f.dp.toPx(), 32f.dp.toPx())
                    }
                ),
            onPauseClick = {
                timerViewModel.resetTimer(context)
            }
        )
        Spacer(Modifier.height(30.dp))
        Text(
            text = "Timer",
            color = gray,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp
        )
        AnimatedContent(
            targetState = secondsLeft > 0,
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
            transitionSpec = {
                slideInVertically { it }.togetherWith(slideOutVertically { -it })
            }
        ) { startTimer ->
            if (startTimer) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timerViewModel.formatTime(secondsLeft),
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 72.sp
                    )
                }
            } else {
                WriteTime(
                    minutes = selectedMinutes,
                    seconds = selectedSeconds,
                    onMinutesChange = { selectedMinutes = it },
                    onSecondsChange = { selectedSeconds = it }
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clickable {
                    if (secondsLeft > 0) {
                        if (timerViewModel.isPaused) {
                            timerViewModel.resumeTimer()
                        } else {
                            timerViewModel.stopTimer()
                        }
                    } else {
                        timerViewModel.startTimer(context, selectedMinutes.toLong() * 60 + selectedSeconds)
                    }
                }
                .width(340.dp)
                .height(60.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(color = Color.White, shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when {
                    secondsLeft > 0 && !timerViewModel.isPaused -> "Stop"
                    secondsLeft > 0 && timerViewModel.isPaused -> "Start"
                    else -> "Start"
                },
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 28.sp
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .width(340.dp)
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clickable {
                        timerViewModel.startTimer(context, 60)
                        selectedTime = 60
                    }
                    .width(96.dp)
                    .height(50.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = if (selectedTime == 60) ringColor else Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "1 mins",
                    color = if (selectedTime == 60) black else gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .clickable {
                        timerViewModel.startTimer(context, 120)
                        selectedTime = 120
                    }
                    .width(96.dp)
                    .height(50.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = if (selectedTime == 120) ringColor else Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "2 mins",
                    color = if (selectedTime == 120) black else gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .clickable {
                        timerViewModel.startTimer(context, 300)
                        selectedTime = 300
                    }
                    .width(96.dp)
                    .height(50.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = if (selectedTime == 300) ringColor else Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "5 mins",
                    color = if (selectedTime == 300) black else gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun TimerGlassBottomBar(
    navController: NavController,
    timerViewModel: TimerViewModel
) {
    Box(Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        TimerScreen(
            modifier = Modifier.layerBackdrop(backdrop),
            timerViewModel = timerViewModel
        )

        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(70.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .drawBackdrop(backdrop = backdrop, shape = { CircleShape }, effects = {
                        vibrancy()
                        blur(2f.dp.toPx())
                        lens(16f.dp.toPx(), 32f.dp.toPx())
                    })
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            navController.navigate("home_screen")
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = black,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Home",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 14.sp,
                    )
                }

                Column(
                    modifier = Modifier
                        .drawBackdrop(backdrop = backdrop, shape = { CircleShape }, effects = {
                            vibrancy()
                            blur(4f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        })
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(onClick = { }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Timer",
                        color = LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 14.sp,
                    )
                }
            }


            Column(
                modifier = Modifier
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        }
                    )
                    .aspectRatio(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = LightGray,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Camera",
                    color = LightGray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Composable
fun TimerRing(
    secondsLeft: Long,
    modifier: Modifier = Modifier,
    totalSeconds: Long,
    onPauseClick: () -> Unit
) {
    val progressTarget = remember(secondsLeft, totalSeconds) {
        if (totalSeconds > 0) secondsLeft.toFloat() / totalSeconds.toFloat() else 0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue =  progressTarget,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )

    Box(
        modifier = modifier
            .width(350.dp)
            .height(220.dp)
            .clickable(onClick = onPauseClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val strokeWidthToPx = 40.dp.toPx()
            val cornerRadiusToPx = 72.dp.toPx()

            drawRoundRect(
                color = Color.White,
                size = size,
                cornerRadius = CornerRadius(cornerRadiusToPx),
                style = Stroke(width = strokeWidthToPx)
            )

            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = size.toRect(),
                        cornerRadius = CornerRadius(cornerRadiusToPx)
                    )
                )
            }

            if (animatedProgress > 0f) {
                val androidPath = path.asAndroidPath()
                val pathMeasure = PathMeasure(androidPath, false)
                val pathLength = pathMeasure.length
                val segmentPath = android.graphics.Path()

                pathMeasure.getSegment(0f, pathLength * animatedProgress, segmentPath, true)

                drawContext.canvas.nativeCanvas.drawPath(
                    segmentPath,
                    android.graphics.Paint().apply {
                        color = ringColor.toArgb()
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = strokeWidthToPx
                        strokeCap = android.graphics.Paint.Cap.ROUND
                        isAntiAlias = true
                    }
                )
            }
        }

        if(secondsLeft > 0) {
            Icon(
                imageVector = Icons.Default.Update,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(100.dp)
                    .drawBackdrop(
                        backdrop = rememberLayerBackdrop(),
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        })
                    .padding(10.dp)
            )
        }
    }
}

@Composable
fun WriteTime(
    minutes: Int,
    seconds: Int,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit,
) {
    val minutesState = rememberFWheelPickerState(initialIndex = minutes)
    val secondsState = rememberFWheelPickerState(initialIndex = seconds)

    LaunchedEffect(minutesState.currentIndex) {
        onMinutesChange(minutesState.currentIndex)
    }

    LaunchedEffect(secondsState.currentIndex) {
        onSecondsChange(secondsState.currentIndex)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FVerticalWheelPicker(
            count = 60,
            state = minutesState,
            modifier = Modifier.width(90.dp),
            itemHeight = 72.dp,
            unfocusedCount = 0,
            focus = {  }
        ) { index ->
            Text(
                text = index.toString().padStart(2, '0'),
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 72.sp
            )
        }

        Text(
            text = ":",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 72.sp,
        )

        FVerticalWheelPicker(
            count = 60,
            state = secondsState,
            modifier = Modifier.width(90.dp),
            itemHeight = 72.dp,
            unfocusedCount = 0,
            focus = { }
        ) { index ->
            Text(
                text = index.toString().padStart(2, '0'),
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 72.sp,
            )
        }
    }
}
