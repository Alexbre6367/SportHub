package com.example.sporthub.ui.components.home

import android.app.Activity
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.components.MainBottomBar
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.FaceViewModel
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.example.sporthub.ui.viewmodel.TimerViewModel
import com.example.sporthub.ui.viewmodel.WorkoutViewModel
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

@Composable
fun StrikeDay(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    timerViewModel: TimerViewModel,
    workoutViewModel: WorkoutViewModel,
    faceViewModel: FaceViewModel,
    screenIndex: Int = 0
) {
    var strikeDay by remember { mutableStateOf(false) }
    val userData by loginViewModel.currentUser.collectAsState()
    val daysStrike = listOf("M", "T", "W", "T", "F", "S", "S")
    val context = LocalContext.current
    val weekProgress by homeViewModel.entryWeek.collectAsState()

    BackHandler(enabled = true) {
        if(strikeDay) {
            strikeDay = false
        } else {
            (context as? Activity)?.finish()
        }
    }

    Box(Modifier.fillMaxSize()) {

        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        MainBottomBar(
            navController,
            loginViewModel,
            homeViewModel,
            timerViewModel,
            workoutViewModel,
            faceViewModel,
            backdrop = backdrop,
            screenIndex = screenIndex,
            onStrike = { strikeDay = true },
        )

        if(strikeDay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { RoundedCornerShape(0.dp) },
                        effects = {
                            vibrancy()
                            blur(8.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        }
                    )
                    .background(Color.Black.copy(alpha = 0.2f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { strikeDay = false }
                    )
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { RoundedCornerShape(36.dp) },
                        effects = {
                            vibrancy()
                            blur(30.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.6f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.7f))
                        }
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { RoundedCornerShape(24.dp) },
                            effects = {
                                vibrancy()
                                blur(34.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            },
                            onDrawSurface = {
                                drawRect(OffWhite, alpha = 0.6f, blendMode = BlendMode.Overlay)
                                drawRect(OffWhite.copy(alpha = 0.7f))
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.LocalFireDepartment,
                        contentDescription = null,
                        tint = black,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))
                Text(
                    text = "${(userData?.strike)?.plus(1)} days streak",
                    color = black,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "You're on fire! Ever day matters for hitting your goal!",
                    color = gray,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    daysStrike.forEachIndexed { index, dayLetter ->
                        val isDayActive = weekProgress.getOrElse(index) { false }
                        Week(
                            days = dayLetter,
                            isActive = isDayActive,
                            activeColor = black,
                            size = 28.dp,
                            strikeDay = true
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .height(58.dp)
                        .fillMaxWidth()
                        .clickable(
                            onClick = { strikeDay = false },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        )
                        .background(
                            color = black,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}