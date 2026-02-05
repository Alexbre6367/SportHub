package com.example.sporthub.ui.screen.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.units.Energy
import androidx.navigation.NavController
import com.example.sporthub.data.healthconnect.HealthState
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.LightPurple
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.Pink40
import com.example.sporthub.ui.theme.activityBlueEnd
import com.example.sporthub.ui.theme.activityBlueStart
import com.example.sporthub.ui.theme.activityGreenEnd
import com.example.sporthub.ui.theme.activityGreenStart
import com.example.sporthub.ui.theme.activityRedEnd
import com.example.sporthub.ui.theme.activityRedStart
import com.example.sporthub.ui.theme.appleBlue
import com.example.sporthub.ui.theme.appleOrange
import com.example.sporthub.ui.theme.applePink
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.colorError
import com.example.sporthub.ui.theme.googleRed
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.himanshoe.charty.circle.CircleChart
import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.common.asGradientChartColor
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.apply
import kotlin.collections.listOf
import kotlin.io.path.Path
import kotlin.math.sin

@Composable
fun HomeScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val healthState = remember { HealthState(context) }


    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(healthState.permissions)) {
            homeViewModel.fetchData()
        }
    }

    LaunchedEffect(Unit) {
        if (healthState.checkPermissions()) {
            homeViewModel.fetchData()
            homeViewModel.strikeDay()
        } else {
            permissionLauncher.launch(healthState.permissions)
        }
    }


    val currentDate = remember {
        val formatter = SimpleDateFormat("d MMMM", Locale.ENGLISH)
        formatter.format(Date())
    }

    val loadedBitmap by loginViewModel.loadedBitmap.collectAsState()
    val userData by loginViewModel.currentUser.collectAsState()
    val step by homeViewModel.steps.collectAsState()
    val sleep by homeViewModel.formatSleep.collectAsState()
    val heart by homeViewModel.heart.collectAsState()
    val oxygen by homeViewModel.oxygen.collectAsState()
    val water by homeViewModel.water.collectAsState()

    val calories by homeViewModel.calories.collectAsState()
    val caloriesGoal = 1000f
    val currentProgress = (calories.inKilocalories.toFloat() / caloriesGoal).coerceIn(0f, 1f)


    val scrollState = rememberScrollState()

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
    ) {
        Spacer(Modifier.statusBarsPadding())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    .clickable(
                        onClick = { navController.navigate("account_screen") },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (loadedBitmap != null) {
                        Image(
                            bitmap = loadedBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = black,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "Today, $currentDate",
                        color = LightGray,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = if (userData?.name != null) {
                            "Welcome Back, ${userData?.name}"
                        } else {
                            "Welcome Back"
                        },
                        color = gray,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .width(60.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Color.Black.copy(alpha = 0.1f),
                            spotColor = Color.Black.copy(alpha = 0.3f)
                        )
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .clickable(
                            onClick = { },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = googleRed,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = (userData?.strike ?: 0).toString(),
                        color = black,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Color.Black.copy(alpha = 0.1f),
                            spotColor = Color.Black.copy(alpha = 0.3f)
                        )
                        .background(color = Color.Black, shape = RoundedCornerShape(16.dp))
                        .clickable(
                            onClick = { },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Column (
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

                ) {
                    CircleChart(homeViewModel)
                }
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
                        text = "${step/1000}/12",
                        color = activityBlueStart,
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
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
                        .clickable { homeViewModel.addWater() },
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .drawBackdrop(
                                backdrop = rememberLayerBackdrop(),
                                shape = { CircleShape },
                                effects = {
                                    vibrancy()
                                    blur(2f.dp.toPx())
                                    lens(16f.dp.toPx(), 32f.dp.toPx())
                                }
                            )
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
                            .drawBackdrop(
                                backdrop = rememberLayerBackdrop(),
                                shape = { CircleShape },
                                effects = {
                                    vibrancy()
                                    blur(2f.dp.toPx())
                                    lens(16f.dp.toPx(), 32f.dp.toPx())
                                }
                            )
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
                    backdrop = rememberLayerBackdrop(),
                    onAddClick = { homeViewModel.addWater() }
                )
            }

        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = Color.White, shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(color = appleOrange, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.DirectionsWalk,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Steps today",
                        color = LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 14.sp,
                    )

                    Text(
                        text = step.toString(),
                        color = gray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                    )
                }
            }

            Spacer(Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp)
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
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(color = LightPurple, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.NightsStay,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Sleep",
                        color = LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 14.sp,
                    )

                    Text(
                        text = sleep,
                        color = gray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = Color.White, shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(color = colorError, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Opacity,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Oxygen",
                        color = LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 14.sp,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = oxygen.toString(),
                            color = gray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp,
                            modifier = Modifier.alignByBaseline()
                        )

                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "%",
                            color = LightGray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                }
            }

            Spacer(Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp)
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
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(color = applePink, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Pulse",
                        color = LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 14.sp,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = heart.toString(),
                            color = gray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp,
                            modifier = Modifier.alignByBaseline()
                        )

                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "bpm",
                            color = LightGray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
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
                Spacer(Modifier.height(4.dp))
                Icon(
                    Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(82.dp)
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
                    text = "3 days",
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
                        text = "/1000",
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
                val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val activeDaysCount = 3

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    days.forEachIndexed { index, day ->
                        DayCircle(
                            dayName = day,
                            isActive = index < activeDaysCount
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(114.dp)
        )
    }
}

@Composable
fun Glass(
    navController: NavController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {

        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        HomeScreen(
            navController,
            loginViewModel,
            homeViewModel,
            modifier = Modifier.layerBackdrop(backdrop)
        )

        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
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
                        .drawBackdrop(backdrop = backdrop, shape = { CircleShape }, effects = {
                            vibrancy()
                            blur(4f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        })
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            scope.launch {
                                scrollState.animateScrollTo(0)
                            }
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
fun WaterButton(
    waterCount: Int,
    backdrop: Backdrop,
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
            .drawBackdrop(
                backdrop = backdrop,
                shape = { CircleShape },
                effects = {
                    vibrancy()
                    blur(2f.dp.toPx())
                    lens(16f.dp.toPx(), 32f.dp.toPx())
                }
            )
            .clip(CircleShape)
            .clickable(
                onClick = { onAddClick() }
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


@Composable
fun DayCircle(
    dayName: String,
    isActive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .drawBackdrop(
                    backdrop = rememberLayerBackdrop(),
                    shape = { CircleShape },
                    effects = {
                        vibrancy()
                        blur(2f.dp.toPx())
                        lens(16f.dp.toPx(), 32f.dp.toPx())
                    }
                )
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isActive) activityRedEnd else Color.White.copy(alpha = 0.0f)),
            contentAlignment = Alignment.Center
        ) {
            if(isActive) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
        Text(
            text = dayName,
            color = LightGray,
            fontSize = 12.sp,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}