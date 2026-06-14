package com.example.sporthub.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.googleRed
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.example.sporthub.ui.viewmodel.TimerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeTopAppBar(
    navController: NavController,
    loginViewModel: LoginViewModel,
    timerViewModel: TimerViewModel,
    onStrike: () -> Unit
) {
    val currentDate = remember {
        val formatter = SimpleDateFormat("d MMMM", Locale.ENGLISH)
        formatter.format(Date())
    }

    val loadedBitmap by loginViewModel.loadedBitmap.collectAsState()
    val userData by loginViewModel.currentUser.collectAsState()

    val secondsLeft by timerViewModel.timerLeft.collectAsStateWithLifecycle()


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .clickable(
                    onClick = { navController.navigate("account_screen") },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                if (loadedBitmap != null) {
                    Image(
                        bitmap = loadedBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = black,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Today, $currentDate",
                    color = LightGray,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = if (userData?.name != null) {
                        "Welcome Back, ${userData?.name}"
                    } else {
                        "Welcome Back"
                    },
                    color = gray,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(secondsLeft > 0) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(60.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Color.Black.copy(alpha = 0.1f),
                            spotColor = Color.Black.copy(alpha = 0.3f)
                        )
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .clickable(
                            onClick = { navController.navigate("timer_screen") },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timerViewModel.formatTime(secondsLeft),
                        color = black,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .width(60.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .clickable(
                        onClick = onStrike,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = googleRed,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${(userData?.strike)?.plus(1) ?: 0}",
                    color = black,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(color = Color.Black, shape = RoundedCornerShape(16.dp))
                    .clickable(
                        onClick = { navController.navigate("timer_screen") },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}