package com.example.sporthub.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.screen.home.HomeScreen
import com.example.sporthub.ui.screen.workout.ChoiceWorkoutsScreen
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.example.sporthub.ui.viewmodel.TimerViewModel
import com.example.sporthub.ui.viewmodel.WorkoutViewModel
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import kotlinx.coroutines.launch

@Composable
fun MainBottomBar(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    timerViewModel: TimerViewModel,
    workoutViewModel: WorkoutViewModel,
    backdrop: LayerBackdrop,
    onStrike: () -> Unit,
    screenIndex: Int = 0
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val userData by loginViewModel.currentUser.collectAsState()

    var selectedScreen by remember(screenIndex) { mutableIntStateOf(screenIndex) }

    val value by animateFloatAsState(
        targetValue = if (selectedScreen == 0) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
        ) {
            AnimatedContent(
                targetState = selectedScreen,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { width -> width })
                            .togetherWith(slideOutHorizontally { width -> -width })
                    } else {
                        (slideInHorizontally { width -> -width })
                            .togetherWith(slideOutHorizontally { width -> width })
                    }
                }
            ) { target ->
                when (target) {
                    0 -> HomeScreen(
                        navController,
                        loginViewModel,
                        homeViewModel,
                        timerViewModel,
                        onStrike = onStrike
                    )

                    1 -> ChoiceWorkoutsScreen(
                        navController,
                        loginViewModel,
                        workoutViewModel
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(58.dp)
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
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(placeable.width, placeable.height) {
                                    placeable.placeRelative(
                                        x = (value * placeable.width).toInt(),
                                        y = 0
                                    )
                                }
                            }
                            .drawBackdrop(backdrop = backdrop, shape = { CircleShape }, effects = {
                                vibrancy()
                                blur(4f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            })
                    )

                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    scope.launch {
                                        scrollState.animateScrollTo(0)
                                        selectedScreen = 0
                                    }
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = null,
                                tint = when (selectedScreen) {
                                    0 -> black
                                    else -> LightGray
                                },
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Home",
                                color = when (selectedScreen) {
                                    0 -> black
                                    else -> LightGray
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 14.sp,
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable(onClick = {
                                    if (userData?.select == true) {
                                        selectedScreen = 1
                                    } else {
                                        navController.navigate("selection_screen")
                                    }
                                }),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = when (selectedScreen) {
                                    1 -> black
                                    else -> LightGray
                                },
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Workout",
                                color = when (selectedScreen) {
                                    1 -> black
                                    else -> LightGray
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 14.sp,
                            )
                        }
                    }
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
                    .clickable { navController.navigate("gemini_screen") }
                    .aspectRatio(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Default.Chat,
                    contentDescription = null,
                    tint = LightGray,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Chat",
                    color = LightGray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 14.sp,
                )
            }
        }
    }
}