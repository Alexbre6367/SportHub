package com.example.sporthub.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sporthub.ui.components.details.InputGlass
import com.example.sporthub.ui.components.home.StrikeDay
import com.example.sporthub.ui.screen.account.AccountGlassBottomBar
import com.example.sporthub.ui.screen.account.DeleteAccountBottomBar
import com.example.sporthub.ui.screen.home.GeminiBar
import com.example.sporthub.ui.screen.home.TimerGlassBottomBar
import com.example.sporthub.ui.screen.login.ForgotPasswordBottomBar
import com.example.sporthub.ui.screen.login.LevelBottomBar
import com.example.sporthub.ui.screen.login.SignInBottomBar
import com.example.sporthub.ui.screen.login.SignUpEmailBottomBar
import com.example.sporthub.ui.screen.login.SignUpPasswordBottomBar
import com.example.sporthub.ui.screen.login.StartBottomBar
import com.example.sporthub.ui.screen.login.WelcomeScreen
import com.example.sporthub.ui.screen.workout.AddWorkoutBar
import com.example.sporthub.ui.screen.workout.CameraTopAppBar
import com.example.sporthub.ui.screen.workout.SelectionTopBar
import com.example.sporthub.ui.viewmodel.CameraViewModel
import com.example.sporthub.ui.viewmodel.GeminiViewModel
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.example.sporthub.ui.viewmodel.TimerViewModel
import com.example.sporthub.ui.viewmodel.WorkoutViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    timerViewModel: TimerViewModel,
    workoutViewModel: WorkoutViewModel,
    geminiViewModel: GeminiViewModel
) {
    var startDestination by remember { mutableStateOf<String?>(null) }
    val userData by loginViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        startDestination = loginViewModel.getStartScreen()
    }

    if (startDestination == null) return


    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination!!,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(route = "level_screen") {
                LevelBottomBar(
                    navController = navController,
                    loginViewModel
                )
            }

            composable(route = "start_screen") {
                StartBottomBar(
                    navController,
                    loginViewModel
                )
            }

            composable(route = "welcome_screen") {
                WelcomeScreen(
                    navController = navController,
                )
            }

            composable(route = "details_up") {
                InputGlass(
                    navController,
                    loginViewModel
                )
            }
            composable(route = "sign_in_screen") {
                SignInBottomBar(
                    navController = navController,
                    loginViewModel = loginViewModel
                )
            }

            composable(route = "sign_up_email_screen") {
                SignUpEmailBottomBar(
                    loginViewModel,
                    navController = navController,
                )
            }

            composable(
                route = "sign_up_password_screen/{encodedEmail}",
                arguments = listOf(
                    navArgument("encodedEmail") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedEmail = backStackEntry.arguments?.getString("encodedEmail") ?: ""
                SignUpPasswordBottomBar(
                    navController,
                    loginViewModel,
                    encodedEmail
                )
            }

            composable(route = "account_screen") {
                AccountGlassBottomBar(
                    navController,
                    loginViewModel
                )
            }

            composable(route = "timer_screen") {
                TimerGlassBottomBar(
                    navController = navController,
                    timerViewModel,
                    loginViewModel
                )
            }

            composable(route = "selection_screen") {
                SelectionTopBar(
                    navController,
                    loginViewModel
                )
            }

            composable(
                route = "home_screen/{screenIndex}",
                arguments = listOf(navArgument("screenIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                })
            ) { backStackEntry ->
                val screenIndex = backStackEntry.arguments?.getInt("screenIndex") ?: 0

                StrikeDay(
                    navController = navController,
                    loginViewModel,
                    homeViewModel,
                    timerViewModel,
                    workoutViewModel,
                    screenIndex
                )
            }

            composable(
                route = "add_workout_screen/{workoutId}",
                arguments = listOf(navArgument("workoutId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: -1
                val allWorkouts by workoutViewModel.workoutList.observeAsState(initial = emptyList())
                val workoutToEdit = allWorkouts.find { it.workout.workoutId == workoutId }

                AddWorkoutBar(
                    navController,
                    workoutViewModel,
                    workoutEdit = workoutToEdit
                )
            }

            composable(route = "gemini_screen") {
                if(userData?.version == 1) {
                    GeminiBar(
                        geminiViewModel = geminiViewModel,
                        navController,
                        loginViewModel
                    )
                } else {
                    StartBottomBar(
                        navController,
                        loginViewModel
                    )
                }
            }

            composable(route = "delete_account_screen") {
                DeleteAccountBottomBar(
                    navController,
                    loginViewModel
                )
            }

            composable(route = "forgot_password_screen") {
                ForgotPasswordBottomBar(
                    navController,
                    loginViewModel
                )
            }

            composable(route = "camera_screen") {
                val cameraViewModel: CameraViewModel = viewModel()
                CameraTopAppBar(
                    cameraViewModel,
                    timerViewModel,
                    navController
                )
            }
        }
    }
}