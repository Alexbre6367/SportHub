package com.example.sporthub.ui.screen.workout

import android.Manifest
import android.app.Activity
import android.os.Build
import android.view.RoundedCorner
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.sporthub.ui.components.camera.CameraPreviewContent
import com.example.sporthub.ui.components.camera.NoPermissions
import com.example.sporthub.ui.components.camera.PoseDetector
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.CameraViewModel
import com.example.sporthub.ui.viewmodel.TimerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel,
    navController: NavHostController,
    timerViewModel: TimerViewModel,
    workout: Boolean,
    onWorkoutChange: (Boolean) -> Unit,
    onEndChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val pose by cameraViewModel.detectedPose.collectAsState()
    var timerJob by remember { mutableStateOf<Job?>(null) }
    val secondsLeft by timerViewModel.timerLeft.collectAsStateWithLifecycle()
    val classificationResult by cameraViewModel.classificationResult.collectAsState()

    LaunchedEffect(workout) {
        if (workout) {
            cameraViewModel.loadPoseClassifier()
            cameraViewModel.start()
        }
    }

    val context = LocalContext.current
    val density = LocalDensity.current

    val screenCornerRadius = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val window = (context as? Activity)?.window
            val insets = window?.decorView?.rootWindowInsets
            val corner = insets?.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT)
            corner?.radius ?: 0
        } else {
            0
        }
    }

    val scope = rememberCoroutineScope()

    val dynamicRoundedCorner = with(density) { screenCornerRadius.toDp() }
    val finalCornerRadius = if (dynamicRoundedCorner > 0.dp) dynamicRoundedCorner else 42.dp

    val cameraPermissions = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
        cameraPermissions.launchPermissionRequest()
    }

    BackHandler {
        navController.navigate("home_screen/1")
    }

    val backdrop = rememberLayerBackdrop {
        drawContent()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite), startY = 0f, endY = 1500f
                )
            )
            .padding(bottom = 12.dp)
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = finalCornerRadius,
                        bottomEnd = finalCornerRadius
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (cameraPermissions.status.isGranted) {
                CameraPreviewContent(cameraViewModel)

                pose?.let { nonNullPose ->
                    PoseDetector(nonNullPose, 480, 640)
                }
            } else {
                NoPermissions(backdrop)
            }
        }

        if (cameraPermissions.status.isGranted) {
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(58.dp)
                        .weight(1f)
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { CircleShape },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            },
                            onDrawSurface = {
                                drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                                drawRect(OffWhite.copy(alpha = 0.5f))
                            }
                        )
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = gray
                    )
                    Spacer(Modifier.width(10.dp))
                    if (!workout) {
                        Text(
                            "Start training",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            color = gray,
                        )
                    } else {
                        Text(
                            classificationResult,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            color = gray,
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { CircleShape },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            },
                            onDrawSurface = {
                                drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                                drawRect(OffWhite.copy(alpha = 0.5f))
                            }
                        )
                        .size(58.dp)
                        .clickable(
                            onClick = {
                                if (secondsLeft > 0 || workout) {
                                    timerJob?.cancel()
                                    timerViewModel.resetTimer(context)
                                    onWorkoutChange(false)
                                    onEndChange(true)
                                    cameraViewModel.stop()
                                } else {
                                    onEndChange(false)
                                    timerViewModel.startTimer(context, 10)
                                    timerJob = scope.launch {
                                        delay(10.seconds)
                                        onWorkoutChange(true)
                                    }
                                }
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (secondsLeft > 0 || workout) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraTopAppBar(
    cameraViewModel: CameraViewModel,
    timerViewModel: TimerViewModel,
    navController: NavHostController,
) {
    val cameraPermissions = rememberPermissionState(Manifest.permission.CAMERA)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            if (cameraPermissions.status.isGranted) {
                val window = (view.context as Activity).window
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    val secondsLeft by timerViewModel.timerLeft.collectAsStateWithLifecycle()
    var workout by remember { mutableStateOf(false) }
    var end by remember { mutableStateOf(false) }

    LaunchedEffect(end) {
        if (end) {
            delay(15.seconds)
            end = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        CameraScreen(
            cameraViewModel,
            navController,
            timerViewModel,
            workout,
            onWorkoutChange = { workout = it },
            onEndChange = { end = it },
            modifier = Modifier.layerBackdrop(backdrop)
        )

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp)
                .height(58.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.5f))
                        }
                    )
                    .size(58.dp)
                    .clickable(
                        onClick = {
                            navController.navigate("home_screen/1")
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .height(58.dp)
                    .weight(1f)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.5f))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Workout",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    color = gray
                )
            }

            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.5f))
                        }
                    )
                    .size(58.dp)
                    .clickable(
                        onClick = { cameraViewModel.switch() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (secondsLeft > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { CircleShape },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(32f.dp.toPx(), 64f.dp.toPx())
                            },
                            onDrawSurface = {
                                drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                                drawRect(OffWhite.copy(alpha = 0.5f))
                            }
                        )
                )
                Text(
                    text = timerViewModel.cameraFormatTime(secondsLeft),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 86.sp,
                    modifier = Modifier.graphicsLayer(blendMode = BlendMode.DstIn)
                )
            }
        }

        val classificationEnd by cameraViewModel.classificationEnd.collectAsState()
        if (end && classificationEnd.isNotBlank()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 20.dp)
                    .height(180.dp)
                    .fillMaxWidth()
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { RoundedCornerShape(24.dp) },
                        effects = {
                            vibrancy()
                            blur(8f.dp.toPx())
                            lens(32.dp.toPx(), 64.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.5f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.5f))
                        }
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = classificationEnd,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 32.sp,
                    color = gray,
                )
            }
        }
    }
}