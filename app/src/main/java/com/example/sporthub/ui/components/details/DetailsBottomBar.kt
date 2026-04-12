package com.example.sporthub.ui.components.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.screen.login.DetailsScreen
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.AuthState
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

@Composable
fun DetailsBottomBar(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    weightState: String,
    heightState: String,
    onWeight: () -> Unit,
    onHeight: () -> Unit,
    openDialog: () -> Unit,
    datePickerState: DatePickerState,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    birthdateState: String,
    modifier: Modifier = Modifier
) {
    val userData by loginViewModel.currentUser.collectAsState()

    var nameState by remember(userData) { mutableStateOf(userData?.name ?: "") }
    var genderState by remember(userData) { mutableStateOf(userData?.gender ?: "") }

    val isFormValid = nameState.isNotBlank() &&
            genderState.isNotBlank() &&
            weightState.isNotBlank() &&
            heightState.isNotBlank() &&
            birthdateState.isNotEmpty()

    val authState by loginViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading


    Box(modifier = modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        DetailsScreen(
            nameState = nameState,
            onNameChange = { nameState = it },
            genderState = genderState,
            onGenderChange = { genderState = it },
            weightState = weightState,
            heightState = heightState,
            birthdateState = birthdateState,
            modifier = Modifier.layerBackdrop(backdrop),
            onWeight = onWeight,
            onHeight = onHeight,
            openDialog = openDialog,
        )

        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
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
                    .size(58.dp)
                    .clickable(
                        onClick = {
                            navController.popBackStack()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .height(58.dp)
                    .weight(1f)
                    .clickable(
                        enabled = !isLoading,
                        onClick = {
                            if (isFormValid) {
                                val weight = weightState.toIntOrNull() ?: 0
                                val height = heightState.toIntOrNull() ?: 0
                                val birthdate = datePickerState.selectedDateMillis ?: userData?.birthdate ?: 0L

                                loginViewModel.detailsUser(
                                    nameState, genderState, weight, height, birthdate
                                )
                                navController.navigate("start_screen")
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    )
                    .background(
                        color = black,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = isLoading,
                    label = "loading_animation",
                ) { targetIsLoading ->
                    if (targetIsLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
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
}
