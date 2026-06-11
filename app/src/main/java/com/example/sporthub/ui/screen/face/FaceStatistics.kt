package com.example.sporthub.ui.screen.face

import android.Manifest
import android.app.Activity
import android.os.Build
import android.view.RoundedCorner
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Face3
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.example.sporthub.ui.components.baseGlass
import com.example.sporthub.ui.components.camera.CameraPreviewContent
import com.example.sporthub.ui.components.camera.NoPermissions
import com.example.sporthub.ui.components.face.FaceDetector
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.CameraViewModel
import com.example.sporthub.ui.viewmodel.FaceViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceStatistics(
    cameraViewModel: CameraViewModel,
    modifier: Modifier = Modifier
) {
    val backdrop = rememberLayerBackdrop {
        drawContent()
    }

    val cameraPermissions = rememberPermissionState(Manifest.permission.CAMERA)

    val density = LocalDensity.current
    val context = LocalContext.current

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

    val dynamicRoundedCorner = with(density) { screenCornerRadius.toDp() }
    val finalCornerRadius = if (dynamicRoundedCorner > 0.dp) dynamicRoundedCorner else 42.dp

    val face by cameraViewModel.detectorFaces.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite), startY = 0f, endY = 1500f
                )
            )
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .navigationBarsPadding()
                .padding(bottom = 250.dp)
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

                FaceDetector(
                    face,
                    480,
                    640
                )
            } else {
                NoPermissions(backdrop)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceTopAppBar(
    cameraViewModel: CameraViewModel,
    faceViewModel: FaceViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var setting by remember { mutableStateOf(false) }

    val faceData by faceViewModel.faceData.observeAsState()
    val widget = faceData?.widget

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        FaceStatistics(
            cameraViewModel,
            modifier = Modifier.layerBackdrop(backdrop)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp)
                    .height(58.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(58.dp)
                        .weight(1f)
                        .baseGlass(backdrop, drawRect = true),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your skin health",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp,
                        color = gray
                    )
                }

                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .baseGlass(backdrop, drawRect = true)
                        .size(58.dp)
                        .clickable(
                            onClick = {
                                setting = !setting
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (setting) {
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .baseGlass(backdrop, drawRect = true)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                            .clickable(
                                onClick = {
                                    navController.navigate("sensitive_skin_screen")
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Face,
                            contentDescription = null,
                            tint = gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Your Sensitive",
                            color = gray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .weight(1f)
                        )
                        Icon(
                            Icons.AutoMirrored.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                            .clickable(
                                onClick = {
                                    faceViewModel.selectWidget()
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if(widget == false) "Add Widget" else "Delete Widget",
                            color = gray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .weight(1f)
                        )
                        Icon(
                            Icons.AutoMirrored.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceBottomBar(
    cameraViewModel: CameraViewModel,
    faceViewModel: FaceViewModel,
    navController: NavController,
) {
    val cameraPermissions = rememberPermissionState(Manifest.permission.CAMERA)

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        cameraPermissions.launchPermissionRequest()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            if (cameraPermissions.status.isGranted) {
                val window = (view.context as Activity).window
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            faceViewModel.stopScan()
        }
    }

    LaunchedEffect(Unit) {
        if (!cameraViewModel.frontCamera.value) {
            cameraViewModel.switch()
            cameraViewModel.switchAnalyze()
        }

        cameraViewModel.start()
    }

    val faceData by faceViewModel.faceData.observeAsState()
    val acne = faceData?.acne ?: 0
    val dryness = faceData?.dryness ?: 0
    val moisture = faceData?.moisture ?: 0

    val scan by faceViewModel.scan.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        FaceTopAppBar(
            cameraViewModel,
            faceViewModel,
            navController,
            modifier = Modifier.layerBackdrop(backdrop)
        )

        if (cameraPermissions.status.isGranted) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .baseGlass(backdrop, shape = RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .baseGlass(backdrop, drawRect = true),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Face3,
                                contentDescription = null,
                                tint = black
                            )
                        }

                        Text(
                            text = "$acne%",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            color = black
                        )
                        Text(
                            text = "Acne",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp,
                            color = LightGray
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .baseGlass(backdrop, shape = RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .baseGlass(backdrop, drawRect = true),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Water,
                                contentDescription = null,
                                tint = black
                            )
                        }

                        Text(
                            text = "$dryness%",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            color = black
                        )
                        Text(
                            text = "Dryness",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp,
                            color = LightGray
                        )
                    }


                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .baseGlass(backdrop, shape = RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .baseGlass(backdrop, drawRect = true),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = black
                            )
                        }

                        Text(
                            text = "$moisture%",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            color = black
                        )
                        Text(
                            text = "Moisture",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp,
                            color = LightGray
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .baseGlass(backdrop)
                            .size(58.dp)
                            .clickable(
                                onClick = {
                                    navController.popBackStack()
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
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
                            .clickable(
                                enabled = !scan,
                                onClick = {
                                    faceViewModel.startScan()
                                    cameraViewModel.imageCapture?.takePicture(
                                        ContextCompat.getMainExecutor(context),
                                        object : ImageCapture.OnImageCapturedCallback() {
                                            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                                val bitmap = imageProxy.toBitmap()
                                                faceViewModel.processCaptureImage(bitmap)
                                                imageProxy.close()
                                            }
                                        }
                                    )
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            )
                            .background(
                                color = black,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = scan,
                            label = "loading_animation"
                        ) { target ->
                            if (target) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "New Scan",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}