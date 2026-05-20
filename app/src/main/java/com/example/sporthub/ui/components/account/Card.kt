package com.example.sporthub.ui.components.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.sporthub.data.auth.authenticate
import com.example.sporthub.ui.theme.AccountGray
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.LightPurple
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Card(
    navController: NavController,
    loginViewModel: LoginViewModel,
    emailState: MutableState<String>,
    onResend: () -> Unit = {},
) {
    val auth = FirebaseAuth.getInstance()
    val isResetPassword by loginViewModel.isResetPassword.collectAsState()
    val context = LocalContext.current
    val activity = context as? FragmentActivity

    Spacer(Modifier.height(20.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .background(
                AccountGray,
                RoundedCornerShape(24.dp)
            )
            .padding(2.dp)
            .padding(top = 14.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "PERSONAL",
            color = gray,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 14.dp)
        )

        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    RoundedCornerShape(22.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .settingCard()
                    .align(Alignment.Start)
                    .clickable(
                        onClick = {
                            navController.navigate("level_screen")
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Face,
                    contentDescription = null,
                    tint = LightBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Your details",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f)
                )
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.fillMaxWidth().height(2.dp).background(AccountGray))
            Row(
                modifier = Modifier
                    .settingCard()
                    .align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DocumentScanner,
                    contentDescription = null,
                    tint = LightPurple,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Face Scans",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f)
                )
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    Spacer(Modifier.height(20.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .background(
                AccountGray,
                RoundedCornerShape(24.dp)
            )
            .padding(2.dp)
            .padding(top = 14.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "SETTING",
            color = gray,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 14.dp)
        )

        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    RoundedCornerShape(22.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .settingCard()
                    .align(Alignment.Start)
                    .clickable(
                        enabled = !isResetPassword,
                        onClick = {
                            loginViewModel.resetPassword(
                                context,
                                onSuccess = {
                                    emailState.value = auth.currentUser?.email ?: ""
                                    onResend()
                                }
                            )
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedContent(
                    targetState = isResetPassword,
                    label = "loading_animation",
                ) { targetIsReset ->
                    if (targetIsReset) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = LightGray,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Password,
                            contentDescription = null,
                            tint = LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Reset Password",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }

            Spacer(Modifier.fillMaxWidth().height(2.dp).background(AccountGray))
            Row(
                modifier = Modifier
                    .settingCard()
                    .align(Alignment.Start)
                    .clickable(
                        onClick = {
                            activity?.let { fragmentActivity ->
                                authenticate(fragmentActivity) {
                                    loginViewModel.signOut()
                                    navController.navigate("welcome_screen") {
                                        popUpTo(0)
                                    }
                                }
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Default.Logout,
                    contentDescription = null,
                    tint = LightGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Logout",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }

            Spacer(Modifier.fillMaxWidth().height(2.dp).background(AccountGray))
            Row(
                modifier = Modifier
                    .settingCard()
                    .align(Alignment.Start)
                    .clickable (
                        onClick = { navController.navigate("delete_Account_screen")},
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = LightGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Delete Account",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }
        }
    }
}

fun Modifier.settingCard(): Modifier = this
    .height(58.dp)
    .fillMaxWidth()
    .padding(horizontal = 20.dp)
    .background(Color.White)