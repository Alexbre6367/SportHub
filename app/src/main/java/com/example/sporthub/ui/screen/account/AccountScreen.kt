package com.example.sporthub.ui.screen.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.components.DialogScreen
import com.example.sporthub.ui.components.account.Card
import com.example.sporthub.ui.components.baseGlass
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.viewmodel.FaceViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@Composable
fun AccountScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    faceViewModel: FaceViewModel,
    emailState: MutableState<String>,
    modifier: Modifier = Modifier,
    onResend: () -> Unit = {},
) {

    LaunchedEffect(Unit) {
        loginViewModel.loadUserData()
    }

    val context = LocalContext.current
    val userData by loginViewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()

    var selectedIndex by remember { mutableIntStateOf(userData?.version ?: 2) }
    LaunchedEffect(userData) {
        userData?.version?.let {
            selectedIndex = it
        }
    }

    val loadedBitmap by loginViewModel.loadedBitmap.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { nonNullUri ->
            loginViewModel.imageUri(nonNullUri, context)
        }
    }

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
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
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

            Spacer(Modifier.height(12.dp))
            Text(
                text = if (userData?.name != null) {
                    "${userData?.name}"
                } else {
                    "Welcome to your account"
                },
                color = black,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable(
                    onClick = { navController.navigate("start_screen") },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Version",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .baseGlass(rememberLayerBackdrop())
                            .height(30.dp)
                            .width(70.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (userData?.version == 1) {
                                "PRO"
                            } else "FREE",
                            color = black,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (userData?.version == 1) {
                        "Personal AI assistant"
                    } else "Basic functions",
                    color = LightGray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }

            Icon(
                if (userData?.version == 1) {
                    Icons.Default.WorkspacePremium
                } else {
                    Icons.Default.Person
                },
                contentDescription = null,
                tint = black,
                modifier = Modifier.size(36.dp)
            )
        }

        Card(
            navController,
            loginViewModel,
            faceViewModel,
            emailState,
            onResend,
        )

        Spacer(Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountBottomBar(
    navController: NavController,
    loginViewModel: LoginViewModel,
    faceViewModel: FaceViewModel
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
            faceViewModel,
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
                    .baseGlass(backdrop)
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
                    .baseGlass(backdrop)
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
