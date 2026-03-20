package com.example.sporthub.ui.screen.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.colorError
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AccountScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
) {

    LaunchedEffect(Unit) {
        loginViewModel.loadUserData()
    }

    val userData by loginViewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()
    val nameScrollState = rememberScrollState()

    var selectedIndex by remember { mutableIntStateOf(userData?.version ?: 2) }
    LaunchedEffect(userData) {
        userData?.version?.let {
            selectedIndex = it
        }
    }

    val isUpdate by loginViewModel.isUpdateVersion.collectAsState()
    val isResetPassword by loginViewModel.isResetPassword.collectAsState()

    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(userData?.birthdate ?: 0))

    val loadedBitmap by loginViewModel.loadedBitmap.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { nonNullUri ->
            loginViewModel.imageUri(nonNullUri)
        }
    }

    val context = LocalContext.current

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
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .clickable(
                        onClick = { launcher.launch("image/*") },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (loadedBitmap != null) {
                    Image(
                        bitmap = loadedBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = black,
                        modifier = Modifier.size(120.dp)
                    )
                }
            }

            Spacer(Modifier.width(20.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .verticalScroll(nameScrollState),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if(userData?.name != null) {
                        "Welcome To Your Account - ${userData?.name}"
                    } else {
                        "Welcome to your account"
                    },
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(Modifier.height(4.dp))
                Text(
                    text = if(userData?.gender != null) {
                        "Gender: ${userData?.gender?.lowercase()}"
                    } else {
                        "Undefined"
                    },
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                )

                Text(
                    text = if(userData?.gender != null) {
                        "Birthdate: $sdf"
                    } else {
                        "Undefined"
                    },
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .clickable(
                        onClick = {
                            navController.navigate("level_screen")
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    )
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .height(58.dp)
                    .weight(1f)
                    .padding(start = 12.dp)
                    .clickable(
                        enabled = !isResetPassword,
                        onClick = {
                            loginViewModel.resetPassword(context)
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
                    targetState = isResetPassword,
                    label = "loading_animation",
                ) { targetIsReset ->
                    if (targetIsReset) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Reset Password",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp),
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Current Weight",
                    color = LightGray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${userData?.weight} kg",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp),
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Current Height",
                    color = LightGray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${userData?.height} cm",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .shadow(
                        elevation = if (selectedIndex == 1) 8.dp else 0.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = if (selectedIndex == 1) Color.White else Color.White.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = if (selectedIndex == 1) 1.dp else 0.dp,
                        color = if (selectedIndex == 1) gray else Color.Transparent,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { selectedIndex = 1 }
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = if (selectedIndex == 1) black else gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = "Pro yearly",
                    color = if (selectedIndex == 1) black else gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
                Text(
                    text = "Personal AI assistant",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedIndex == 1) black else gray,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "First 7 day free",
                    color = if (selectedIndex == 1) black else gray,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .shadow(
                        elevation = if (selectedIndex == 2) 8.dp else 0.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = if (selectedIndex == 2) Color.White else Color.White.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = if (selectedIndex == 2) 1.dp else 0.dp,
                        color = if (selectedIndex == 2) gray else Color.Transparent,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { selectedIndex = 2 }
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = if (selectedIndex == 2) black else gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = "Free version",
                    color = if (selectedIndex == 2) black else gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
                Text(
                    text = "Basic functions",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedIndex == 2) black else gray,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "First ∞ day free",
                    color = if (selectedIndex == 2) black else gray,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clickable(
                    onClick = {
                        if (selectedIndex != userData?.version) {
                            loginViewModel.version(selectedIndex)
                        }
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                )
                .shadow(
                    elevation = if(selectedIndex != userData?.version) 8.dp else 0.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(
                    color = if (selectedIndex != userData?.version) black else black.copy(alpha = 0.5f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = isUpdate,
                label = "loading_animation",
            ) { targetIsUpdate ->
                if (targetIsUpdate) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text =
                            when (selectedIndex) {
                                userData?.version -> "Your current level"
                                1 -> "Upgrade to Pro for $5.99"
                                else -> "Switch to Free version"
                            },
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clickable(
                        onClick = {
                            loginViewModel.signOut()
                            navController.navigate("welcome_screen") {
                                popUpTo(0)
                            }
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Output,
                    tint = black,
                    contentDescription = null
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Logout",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clickable{ navController.navigate("delete_Account_screen") },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Delete,
                    tint = colorError,
                    contentDescription = null
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Delete",
                    color = colorError,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun AccountGlassBottomBar(
    navController: NavController,
    loginViewModel: LoginViewModel,
) {
    Box(Modifier.fillMaxSize()) {

        val backdrop = rememberLayerBackdrop {
            drawContent()
        }
        val userData by loginViewModel.currentUser.collectAsState()

        AccountScreen(
            navController,
            loginViewModel,
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
                            navController.navigate("home_screen")
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
                            if(userData?.select == true) {
                                navController.navigate("workout_screen")
                            } else{
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
                    .clickable{ navController.navigate("gemini_screen") }
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

