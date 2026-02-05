package com.example.sporthub.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sporthub.ui.screen.home.AccountScreen
import com.example.sporthub.ui.screen.home.Glass
import com.example.sporthub.ui.screen.login.DetailsScreen
import com.example.sporthub.ui.screen.login.LevelScreen
import com.example.sporthub.ui.screen.login.SignInScreen
import com.example.sporthub.ui.screen.login.SignUpEmailScreen
import com.example.sporthub.ui.screen.login.SignUpPasswordScreen
import com.example.sporthub.ui.screen.login.StartScreen
import com.example.sporthub.ui.screen.login.WelcomeScreen
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel
) {
    var startDestination by remember { mutableStateOf<String?>(null) }

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
                LevelScreen(
                    navController = navController,
                    loginViewModel
                )
            }

            composable(route = "start_screen") {
                StartScreen(
                    navController = navController,
                    loginViewModel
                )
            }

            composable(route = "welcome_screen") {
                WelcomeScreen(
                    navController = navController,
                )
            }

            composable(route = "details_up") {
                DetailsScreen(
                    navController = navController,
                    loginViewModel
                )
            }
            composable(route = "sign_in_screen") {
                SignInScreen(
                    navController = navController,
                    loginViewModel = loginViewModel
                )
            }

            composable(route = "sign_up_email_screen") {
                SignUpEmailScreen(
                    navController = navController,
                    loginViewModel = loginViewModel
                )
            }

            composable(
                route = "sign_up_password_screen/{encodedEmail}",
                arguments = listOf(
                    navArgument("encodedEmail") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedEmail = backStackEntry.arguments?.getString("encodedEmail") ?: ""
                SignUpPasswordScreen(
                    navController,
                    encodedEmail,
                    loginViewModel
                )
            }

            composable(route = "home_screen") {
                Glass(
                    navController = navController,
                    loginViewModel,
                    homeViewModel
                )
            }

            composable(route = "account_screen") {
                AccountScreen(
                    navController = navController,
                    loginViewModel
                )
            }
        }
    }
}