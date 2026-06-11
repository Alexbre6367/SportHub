package com.example.sporthub.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.components.BottomBarContinue
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.AuthState
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop


@Composable
fun LevelScreen(
    selectedIndex: Int,
    onLevelSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite),
                    startY = 0f,
                    endY = 1500f
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 70.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "What is the best define your activity level?",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(52.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .border(
                    width = if (selectedIndex == 1) 1.dp else 0.dp,
                    color = if (selectedIndex == 1) gray else Color.Transparent,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { onLevelSelected(1) }
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(18.dp)
            ) {
                Text(
                    "Sedentary",
                    color = black,
                    fontSize = 26.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    "Minimal physical activity",
                    color = gray,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Icon(
                Icons.Default.Chair,
                contentDescription = null,
                tint = gray,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(18.dp)
                    .size(60.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .border(
                    width = if (selectedIndex == 2) 1.dp else 0.dp,
                    color = if (selectedIndex == 2) gray else Color.Transparent,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { onLevelSelected(2) }
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(18.dp)
            ) {
                Text(
                    "Lightly Active",
                    color = black,
                    fontSize = 26.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    "Sports 1 days a week",
                    color = gray,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Icon(
                Icons.AutoMirrored.Default.DirectionsWalk,
                contentDescription = null,
                tint = gray,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(18.dp)
                    .size(60.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .border(
                    width = if (selectedIndex == 3) 1.dp else 0.dp,
                    color = if (selectedIndex == 3) gray else Color.Transparent,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { onLevelSelected(3) }
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(18.dp)
            ) {
                Text(
                    "Very Active",
                    color = black,
                    fontSize = 26.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    "Sports 2-4 days a week",
                    color = gray,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Icon(
                Icons.AutoMirrored.Default.DirectionsRun,
                contentDescription = null,
                tint = gray,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(18.dp)
                    .size(60.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(120.dp)
        )
    }
}

@Composable
fun LevelBottomBar(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val userData by loginViewModel.currentUser.collectAsState()

    val authState by loginViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    LaunchedEffect(userData) {
        userData?.let {
            if(selectedIndex == -1) selectedIndex = it.level
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        LevelScreen(
            selectedIndex = selectedIndex,
            onLevelSelected = { selectedIndex = it },
            modifier = Modifier.layerBackdrop(backdrop)
        )

        BottomBarContinue(
            backdrop,
            navController = navController,
            onClick = {
                if(selectedIndex != -1) {
                    loginViewModel.levelUser(selectedIndex)
                    navController.navigate("details_up")
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            isLoading = isLoading
        )
    }
}


