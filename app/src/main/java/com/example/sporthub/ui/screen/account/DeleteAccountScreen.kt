package com.example.sporthub.ui.screen.account

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.components.BottomBarContinue
import com.example.sporthub.ui.components.mainColumn
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@Composable
fun DeleteAccountScreen(
    loginViewModel: LoginViewModel,
    passwordState: MutableState<String>
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val isGoogleAccount by loginViewModel.isGoogleAccount.collectAsState()

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .mainColumn(
                scrollState,
                onClick = { focusManager.clearFocus() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = if(isGoogleAccount) "Enter DELETE to confirm" else "Enter your password",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = if(isGoogleAccount) "Confirm that you want to delete your account." else "To delete your account confirm your password.",
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
                        text = if(isGoogleAccount) "Enter DELETE" else "Enter password",
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
fun DeleteAccountBottomBar(
    navController: NavController,
    loginViewModel: LoginViewModel,
) {
    val passwordState = remember { mutableStateOf("") }
    val isDelete by loginViewModel.isDelete.collectAsState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        DeleteAccountScreen(
            loginViewModel,
            passwordState
        )

        BottomBarContinue(
            backdrop,
            navController = navController,
            onClick = {
                if (passwordState.value.isNotEmpty()) {
                    if(loginViewModel.isGoogleAccount.value) {
                        if(passwordState.value == "DELETE") {
                            loginViewModel.deleteAccount(
                                password = null,
                                onSuccess = {
                                    passwordState.value = ""
                                    navController.navigate("welcome_screen") {
                                        popUpTo(0)
                                    }
                                },
                                onError = {
                                    passwordState.value = ""
                                    Toast.makeText(
                                        context,
                                        "Invalid value or network error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    } else {
                        loginViewModel.deleteAccount(
                            password = passwordState.value,
                            onSuccess = {
                                navController.navigate("welcome_screen") {
                                    popUpTo(0)
                                }
                            },
                            onError = {
                                Toast.makeText(
                                    context,
                                    "Invalid value or network error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            isLoading = isDelete
        )
    }
}
