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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.components.BottomBarContinue
import com.example.sporthub.ui.components.mainColumn
import com.example.sporthub.ui.theme.appleBlue
import com.example.sporthub.ui.theme.appleOrange
import com.example.sporthub.ui.theme.applePink
import com.example.sporthub.ui.theme.applePurple
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.colorError
import com.example.sporthub.ui.theme.googleBlue
import com.example.sporthub.ui.theme.googleGreen
import com.example.sporthub.ui.theme.googleRed
import com.example.sporthub.ui.theme.googleYellow
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.AuthState
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SignInScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    errorLogin: Boolean,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester
) {

    var passwordVisibility by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    var openDialog by remember { mutableStateOf(false) }

    val authState by loginViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    Column(
        modifier = modifier
            .mainColumn(
                scrollState,
                onClick = { focusManager.clearFocus() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome Back",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Log in to our app now!",
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
            Text(
                text = "Email",
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                singleLine = true,
                placeholder = { Text("User@gmail.com", style = MaterialTheme.typography.titleLarge.copy(color = gray), fontSize = 16.sp) },
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 16.sp,
                    color = gray
                ),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = if(errorLogin) colorError else gray,
                    unfocusedIndicatorColor = if(errorLogin) colorError else gray,
                    cursorColor = gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Password",
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                singleLine = true,
                placeholder = { Text("Enter password", style = MaterialTheme.typography.titleLarge.copy(color = gray), fontSize = 16.sp) },
                visualTransformation = if(passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if(passwordState.value.isNotBlank()) {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility}) {
                            Icon(
                                imageVector = if(passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = gray
                            )
                        }
                    }
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
                    focusedIndicatorColor = if(errorLogin) colorError else gray,
                    unfocusedIndicatorColor = if(errorLogin) colorError else gray,
                    cursorColor = gray
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.height(32.dp))
        Text(
            text = "Or continue with",
            color = gray,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clickable(
                    enabled = !isLoading,
                    onClick = {
                        loginViewModel.signInGoogle()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                )
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign in with Google",
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            googleBlue,
                            googleRed,
                            googleYellow,
                            googleGreen
                        )
                    ),
                    fontSize = 18.sp
                )
            )
        }

        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clickable(
                    onClick = {
                        openDialog = !openDialog
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                )
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign in with Apple",
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            appleBlue,
                            applePurple,
                            applePink,
                            appleOrange
                        )
                    ),
                    fontSize = 18.sp
                )
            )
        }

        Spacer(Modifier.height(32.dp))
        Text(
            text = "Forgot your password?",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable{
                    navController.navigate("forgot_password_screen")
                }
        )

        if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                title = { Text(text = "In development") },
                confirmButton = {
                    Button(
                        onClick = { openDialog = false },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                        Text("Ok")
                    }
                },
                containerColor = Color.DarkGray,
                titleContentColor = Color.White,
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
fun SignInBottomBar(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val authState by loginViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var errorLogin by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            errorLogin = false
        }
    }

    LaunchedEffect(errorLogin) {
        if(errorLogin) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    LaunchedEffect(authState) {
        if(authState == AuthState.Success) {
            navController.navigate(loginViewModel.getStartScreen()) {
                popUpTo(0)
            }
        }
    }

    LaunchedEffect(authState) {
        if(authState == AuthState.Error) {
            errorLogin = true
            delay(5000.milliseconds)
            errorLogin = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        SignInScreen(
            navController,
            loginViewModel,
            emailState,
            passwordState,
            errorLogin = errorLogin,
            modifier = Modifier.layerBackdrop(backdrop),
            focusRequester
        )

        BottomBarContinue(
            backdrop,
            navController = navController,
            onClick = {
                errorLogin = false
                if (emailState.value.isNotEmpty() && passwordState.value.isNotEmpty()) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    loginViewModel.signIn(emailState.value, passwordState.value)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            isLoading = isLoading
        )
    }
}
