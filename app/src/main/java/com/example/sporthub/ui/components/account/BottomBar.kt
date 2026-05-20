package com.example.sporthub.ui.components.account

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.components.DialogScreen
import com.example.sporthub.ui.screen.account.AccountScreen
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountGlassBottomBar(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val emailState = remember { mutableStateOf("") }
    var dialogScreen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        if (dialogScreen) {
            ModalBottomSheet(
                onDismissRequest = { dialogScreen = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = Color.White
            ) {
                DialogScreen(
                    emailState,
                    onResend = {
                        loginViewModel.resetPassword(
                            context,
                            emailState.value,
                            onSuccess = { dialogScreen = true })
                    },
                    onDismiss = { dialogScreen = false },
                )
            }
        }
        val userData by loginViewModel.currentUser.collectAsState()

        AccountScreen(
            navController,
            loginViewModel,
            emailState,
            onResend = { dialogScreen = true },
            modifier = Modifier.layerBackdrop(backdrop)
        )

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
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        }
                    )
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            navController.navigate("home_screen/0")
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Home",
                        color = LightGray,
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
                                navController.navigate("home_screen/1")
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
                        tint = LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Workout",
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