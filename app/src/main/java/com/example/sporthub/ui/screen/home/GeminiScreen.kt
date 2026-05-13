package com.example.sporthub.ui.screen.home

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.components.MessageBox
import com.example.sporthub.ui.components.chat.EmptyStateGemini
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.GeminiViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

@Composable
fun GeminiScreen(
    geminiViewModel: GeminiViewModel,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val message by geminiViewModel.message.collectAsState()
    val isLoading by geminiViewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(message.size) {
        if(message.isNotEmpty()) {
            listState.animateScrollToItem(message.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite), startY = 0f, endY = 1500f
                )
            )
            .clickable(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        if(message.isEmpty()) {
            EmptyStateGemini(
                loginViewModel,
                geminiViewModel
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(
                    top =  129.dp,
                    bottom = 120.dp
                )
            ) {
                items(message) { message ->
                    MessageBox(message)
                }

                if (isLoading) {
                    item {
                        Text(
                            text = "Loading...",
                            color = gray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GeminiBar(
    geminiViewModel: GeminiViewModel,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    DisposableEffect(Unit) {
        onDispose {
            geminiViewModel.clearFile()
        }
    }

    val loadedBitmap by geminiViewModel.loadedBitmap.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { nonNullUri ->
            geminiViewModel.attachedFile(nonNullUri)
        }
    }


    var promptText by remember { mutableStateOf("") }
    val isLoading by geminiViewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        GeminiScreen(
            geminiViewModel,
            loginViewModel,
            modifier = Modifier.layerBackdrop(backdrop)
        )

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp)
                .height(58.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
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
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.5f))
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

            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .height(58.dp)
                    .weight(1f)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.5f))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ask Anything",
                    color = gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .imePadding()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            if(loadedBitmap != null) {
                Box(
                    modifier = Modifier
                        .width(116.dp)
                        .height(58.dp)
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { CircleShape },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            },
                            onDrawSurface = {
                                drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                                drawRect(OffWhite.copy(alpha = 0.5f))
                            }
                        )
                        .padding(4.dp)
                        .align(Alignment.Start),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = loadedBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .heightIn(min = 58.dp)
                        .weight(1f)
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { CircleShape },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            },
                            onDrawSurface = {
                                drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                                drawRect(OffWhite.copy(alpha = 0.5f))
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = promptText,
                        onValueChange = { promptText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .padding(start = 10.dp, end = 20.dp),
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 18.sp,
                            color = gray,
                            textAlign = TextAlign.Start
                        ),
                        maxLines = 5,
                        cursorBrush = SolidColor(gray),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .drawBackdrop(
                                            backdrop = backdrop,
                                            shape = { CircleShape },
                                            effects = {
                                                vibrancy()
                                                blur(2f.dp.toPx())
                                                lens(16f.dp.toPx(), 32f.dp.toPx())
                                            },
                                            onDrawSurface = {
                                                drawRect(
                                                    OffWhite,
                                                    alpha = 0.4f,
                                                    blendMode = BlendMode.Overlay
                                                )
                                                drawRect(OffWhite.copy(alpha = 0.5f))
                                            }
                                        )
                                        .clickable(
                                            onClick = {
                                                if(loadedBitmap != null) {
                                                    geminiViewModel.clearFile()
                                                } else {
                                                    launcher.launch("image/*")
                                                }
                                            },
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if(loadedBitmap != null) {
                                        Icon(
                                            Icons.Default.Clear,
                                            contentDescription = null,
                                            tint = gray
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null,
                                            tint = gray
                                        )
                                    }
                                }
                                Spacer(Modifier.width(10.dp))
                                if (promptText.isEmpty()) {
                                    Text(
                                        "Type something..",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 18.sp,
                                        color = gray,
                                    )
                                }

                                innerTextField()
                            }
                        }
                    )
                }

                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { CircleShape },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            },
                            onDrawSurface = {
                                drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                                drawRect(OffWhite.copy(alpha = 0.5f))
                            }
                        )
                        .size(58.dp)
                        .clickable(
                            onClick = {
                                if (promptText.isNotBlank()) {
                                    geminiViewModel.message(promptText)
                                    promptText = "" //очистка
                                    geminiViewModel.clearFile()
                                }
                            },
                            enabled = !isLoading,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}