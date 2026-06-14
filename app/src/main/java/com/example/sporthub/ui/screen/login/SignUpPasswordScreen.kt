package com.example.sporthub.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.colorError
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.AuthState
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SignUpPasswordScreen(
    encodedEmail: String,
    passwordState: MutableState<String>,
    errorLogin: Boolean,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    val email = remember {
        URLDecoder.decode(
            encodedEmail,
            StandardCharsets.UTF_8.toString()
        )
    }

    Column(
        modifier = modifier
            .mainColumn(
                scrollState,
                onClick = { focusManager.clearFocus() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Set Your Password",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Choose a strong password to protect your data",
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
            Spacer(Modifier.height(16.dp))
            Text(
                text = "$email",
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (passwordState.value.isNotBlank()) {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = gray
                            )
                        }
                    }
                },
                placeholder = {
                    Text(
                        "Enter password",
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
                    focusedIndicatorColor = if(errorLogin) colorError else gray,
                    unfocusedIndicatorColor = if(errorLogin) colorError else gray,
                    cursorColor = gray
                ),
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
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
fun SignUpPasswordBottomBar(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    encodedEmail: String,
) {
    val passwordState = remember { mutableStateOf("") }
    val email = remember {
        URLDecoder.decode(
            encodedEmail,
            StandardCharsets.UTF_8.toString()
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val authState by loginViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    val context = LocalContext.current

    LaunchedEffect(authState) {
        if(authState is AuthState.Success) {
            navController.navigate("level_screen") {
                popUpTo(0)
            }
        } else if(authState is AuthState.Error) {
            Toast.makeText(
                context,
                "Error creating account",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    var errorLogin by remember { mutableStateOf(false) }
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

        SignUpPasswordScreen(
            encodedEmail,
            passwordState,
            errorLogin,
            modifier = Modifier.layerBackdrop(backdrop),
            focusRequester
        )

        BottomBarContinue(
            backdrop,
            navController = navController,
            onClick = {
                errorLogin = false
                if(passwordState.value.isNotBlank()) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    loginViewModel.signUp(email, passwordState.value)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            isLoading = isLoading
        )
    }
}

