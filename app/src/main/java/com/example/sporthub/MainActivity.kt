package com.example.sporthub

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.sporthub.navigation.AppNavGraph
import com.example.sporthub.ui.theme.SportHubTheme
import com.example.sporthub.ui.viewmodel.GeminiViewModel
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.example.sporthub.ui.viewmodel.TimerViewModel
import com.example.sporthub.ui.viewmodel.WorkoutViewModel

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportHubTheme {
                val navController = rememberNavController()
                val loginViewModel: LoginViewModel = viewModel()
                val homeViewModel: HomeViewModel = viewModel()
                val timerViewModel: TimerViewModel = viewModel()
                val workoutViewModel: WorkoutViewModel = viewModel()
                val geminiViewModel: GeminiViewModel = viewModel()
                AppNavGraph(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    homeViewModel = homeViewModel,
                    timerViewModel = timerViewModel,
                    workoutViewModel = workoutViewModel,
                    geminiViewModel
                )
            }
        }
    }
}

