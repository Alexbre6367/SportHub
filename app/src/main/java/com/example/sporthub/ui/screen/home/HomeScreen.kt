package com.example.sporthub.ui.screen.home

import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.health.connect.client.PermissionController
import androidx.navigation.NavController
import com.example.sporthub.data.health.HealthState
import com.example.sporthub.ui.components.home.ActivityRing
import com.example.sporthub.ui.components.home.Calories
import com.example.sporthub.ui.components.home.HealthStats
import com.example.sporthub.ui.components.home.HomeTopAppBar
import com.example.sporthub.ui.components.home.WaterTarget
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.viewmodel.HomeViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.example.sporthub.ui.viewmodel.TimerViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    timerViewModel: TimerViewModel,
    onStrike: () -> Unit,
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

    val activityPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if (isGranted.containsValue(true)) {
            homeViewModel.fetchData()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val check = ContextCompat.checkSelfPermission(
                context,
                POST_NOTIFICATIONS
            )
            if (check != PackageManager.PERMISSION_GRANTED) {
                activityPermissionLauncher.launch(
                    arrayOf(
                        ACTIVITY_RECOGNITION,
                        POST_NOTIFICATIONS
                    )
                )
            }
        }

        if (healthState.checkPermissions()) {
            homeViewModel.fetchData()
        } else {
            permissionLauncher.launch(healthState.permissions)
        }
    }

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
            .statusBarsPadding()
    ) {
        HomeTopAppBar(
            navController,
            loginViewModel,
            timerViewModel,
            onStrike = onStrike
        )

        Spacer(Modifier.height(14.dp))
        ActivityRing(homeViewModel)

        Spacer(Modifier.height(20.dp))
        WaterTarget(homeViewModel)

        Spacer(Modifier.height(20.dp))
        HealthStats(homeViewModel)

        Spacer(Modifier.height(20.dp))
        Calories(homeViewModel)

        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(120.dp)
        )
    }
}