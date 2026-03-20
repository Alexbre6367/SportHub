package com.example.sporthub.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.AuthState
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun SignUpPasswordScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    encodedEmail: String,
    passwordState: MutableState<String>
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
        modifier = Modifier
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
            .padding(horizontal = 28.dp)
            .padding(top = 70.dp),
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
                    focusedIndicatorColor = gray,
                    unfocusedIndicatorColor = gray,
                    cursorColor = gray
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
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

    val authState by loginViewModel.authState.collectAsState()
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

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        SignUpPasswordScreen(
            navController,
            loginViewModel,
            encodedEmail,
            passwordState
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
                        onClick = {
                            if(passwordState.value.isNotBlank()) {
                                loginViewModel.signUp(email, passwordState.value)
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

