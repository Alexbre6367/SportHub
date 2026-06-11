package com.example.sporthub.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.components.BottomBarContinue
import com.example.sporthub.ui.components.DialogScreen
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@Composable
fun ForgotPasswordScreen(
    emailState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite),
                    startY = 0f,
                    endY = 1500f
                )
            )
            .verticalScroll(scrollState)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Enter your email",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Enter the email address of the account for which you want to recover the password.",
            color = gray,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )

        Spacer(Modifier.height(38.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                singleLine = true,
                placeholder = {
                    Text(
                        "Enter email",
                        style = MaterialTheme.typography.titleLarge.copy(color = gray),
                        fontSize = 16.sp
                    )
                },
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 16.sp,
                    color = gray
                ),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = gray,
                    unfocusedIndicatorColor = gray,
                    cursorColor = gray
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordBottomBar(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val emailState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val isResetPassword by loginViewModel.isResetPassword.collectAsState()

    var dialogScreen by remember { mutableStateOf(false) }

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

        ForgotPasswordScreen(
            emailState,
            modifier = Modifier.layerBackdrop(backdrop)
        )


        BottomBarContinue(
            backdrop,
            navController = navController,
            onClick = {
                loginViewModel.resetPassword(
                    context,
                    emailState.value,
                    onSuccess = { dialogScreen = true }
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            isLoading = isResetPassword
        )
    }
}
