package com.example.sporthub.ui.screen.workout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.data.sporthub.WorkoutWithExercises
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.example.sporthub.ui.viewmodel.WorkoutViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChoiceWorkoutsScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    workoutViewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val workouts by workoutViewModel.workoutList.observeAsState(initial = emptyList())

    BackHandler{
        navController.navigate("home_screen/0")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite), startY = 0f, endY = 1500f
                )
            )
            .padding(horizontal = 20.dp)
            .statusBarsPadding(),
    ) {
        WorkoutTopAppBar(
            navController,
            loginViewModel,
        )

        if(workouts.isEmpty()) {
            EmptyStateWorkout()
        } else {
            Spacer(Modifier.height(14.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(workouts) { workout ->
                    WorkoutItem(
                        workoutWithExercises = workout,
                        onClick = {
                            navController.navigate("add_workout_screen/${workout.workout.workoutId}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutTopAppBar(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val currentDate = remember {
        val formatter = SimpleDateFormat("d MMMM", Locale.ENGLISH)
        formatter.format(Date())
    }

    val loadedBitmap by loginViewModel.loadedBitmap.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .width(220.dp)
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
                        contentScale = ContentScale.Crop
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
                    text = "Welcome to Your Training",
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
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .clickable(
                        onClick = { navController.navigate("add_workout_screen/-1") },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    tint = black,
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(60.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(color = Color.Black, shape = RoundedCornerShape(16.dp))
                    .clickable(
                        onClick = { navController.navigate("camera_screen") },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun WorkoutItem(
    workoutWithExercises: WorkoutWithExercises,
    onClick: () -> Unit
) {
    val randomIcon = listOf(
        Icons.Default.FitnessCenter,
        Icons.Default.SportsGymnastics,
        Icons.Default.SportsHandball,
        Icons.Default.SportsTennis,
        Icons.Default.SportsMartialArts,
    ).random()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            workoutWithExercises.workout.name,
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Top)
        )

        Icon(
            randomIcon,
            tint = black,
            contentDescription = null
        )
    }
}

@Composable
fun EmptyStateWorkout() {
    Column(
        modifier = Modifier.fillMaxSize().offset(y = (-74).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.FitnessCenter,
            tint = gray,
            contentDescription = null,
            modifier = Modifier.size(160.dp).align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(20.dp))
        Text(
            text = "Your workouts will go here",
            color = gray,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        )
    }
}