package com.example.sporthub.ui.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.activityRedEnd
import com.example.sporthub.ui.theme.appleOrange
import com.example.sporthub.ui.theme.applePink
import com.example.sporthub.ui.theme.applePurple
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.colorError
import com.example.sporthub.ui.theme.googleGreen
import com.example.sporthub.ui.theme.googleYellow
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.theme.ringColor
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

@Composable
fun SelectionScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    select: Int,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite), startY = 0f, endY = 1500f
                )
            )
            .verticalScroll(scrollState)
            .padding(vertical = 12.dp)
    ) {
        Spacer(Modifier.height(122.dp))
        AnimatedContent(
            targetState = select,
            label = "selection_content",
            transitionSpec = {
                if(targetState == 2) {
                    slideInHorizontally { width -> width }
                        .togetherWith(slideOutHorizontally { width -> -width })
                } else {
                    slideInHorizontally { width -> -width }
                        .togetherWith(slideOutHorizontally { width -> width })
                }
            }
        ) { currentSelect ->
            if (currentSelect == 1) Pro() else Free()
        }

        Spacer(Modifier.weight(1f))
        SelectionBottomBar(
            navController,
            loginViewModel,
            select,
        )
    }
}

@Composable
fun SelectionTopBar(
    navController: NavController,
    loginViewModel: LoginViewModel,
) {
    val userData by loginViewModel.currentUser.collectAsState()
    var select by remember { mutableIntStateOf(userData?.version ?: 2) }

    val value by animateFloatAsState(
        targetValue = if(select == 1) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(Modifier.fillMaxSize()) {
        SelectionScreen(navController, loginViewModel, select)

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 58.dp)
                .height(58.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.1f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(color = Color.White, shape = CircleShape)
                .padding(6.dp)
                .align(Alignment.TopCenter),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(
                                x = (value * placeable.width).toInt(),
                                y = 0
                            )
                        }
                    }
                    .drawBackdrop(
                        backdrop = rememberLayerBackdrop(),
                        shape = { CircleShape },
                        effects = { }
                    )
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { select = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pro+",
                        color = if (select == 1) black else LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { select = 2 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Free",
                        color = if (select == 2) black else LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun Pro() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .selectionCardStyle()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Personalized daily recommendations from AI",
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 26.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { CircleShape },
                            effects = { }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Adviсe",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { CircleShape },
                            effects = { }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Support",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .selectionCardStyle()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = LightBlue,
                    modifier = Modifier.size(120.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .drawBackdrop(
                        backdrop = rememberLayerBackdrop(),
                        shape = { RoundedCornerShape(24.dp) },
                        effects = { }
                    )
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        tint = appleOrange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Your",
                        color = LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 14.sp,
                    )
                }

                Text(
                    text = "Personal Plan",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 28.dp)
                )

                Spacer(Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        tint = activityRedEnd,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Your",
                        color = LightGray,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 14.sp,
                    )
                }

                Text(
                    text = "Assistant",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 28.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .selectionCardStyle()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = applePurple,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = "Smart workouts",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
                Text(
                    text = "With AI",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp
                )
                Spacer(Modifier.padding(2.dp))
                Text(
                    text = "Personal chat",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.width(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .selectionCardStyle()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Fastfood,
                    contentDescription = null,
                    tint = applePink,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = "Сalorie counting",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
                Text(
                    text = "With AI",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = "Weight loss plan",
                    color = black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}


@Composable
fun Free() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .selectionCardStyle()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = ringColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.padding(12.dp))
                    Text(
                        text = "Between sets",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Timer",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp
                    )
                    Spacer(Modifier.padding(4.dp))
                    Text(
                        text = "For each approach",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp
                    )
                }
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .selectionCardStyle()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.MonitorHeart,
                        contentDescription = null,
                        tint = colorError,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.padding(12.dp))
                    Text(
                        text = "Health data",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Monitoring",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp
                    )
                    Spacer(Modifier.padding(4.dp))
                    Text(
                        text = "During training",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(Modifier.width(20.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .selectionCardStyle()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.NoteAlt,
                        contentDescription = null,
                        tint = LightBlue,
                        modifier = Modifier.size(120.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { RoundedCornerShape(24.dp) },
                            effects = { }
                        )
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Circle,
                            tint = googleGreen,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Your",
                            color = LightGray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 14.sp,
                        )
                    }

                    Text(
                        text = "Personal Plan",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 28.dp)
                    )

                    Spacer(Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Circle,
                            tint = googleYellow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Your",
                            color = LightGray,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 14.sp,
                        )
                    }

                    Text(
                        text = "Result",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 28.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .selectionCardStyle()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Set a goal and achieve it",
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 26.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { CircleShape },
                            effects = { }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Plane",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { CircleShape },
                            effects = { }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Target",
                        color = black,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

fun Modifier.selectionCardStyle(): Modifier = this
    .shadow(
        elevation = 8.dp,
        shape = RoundedCornerShape(24.dp),
        ambientColor = Color.Black.copy(alpha = 0.1f),
        spotColor = Color.Black.copy(alpha = 0.3f)
    )
    .background(color = Color.White, shape = RoundedCornerShape(24.dp))

@Composable
fun SelectionBottomBar(
    navController: NavController,
    loginViewModel: LoginViewModel,
    select: Int,
) {
    val userData by loginViewModel.currentUser.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .drawBackdrop(
                    backdrop = rememberLayerBackdrop(),
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
                        loginViewModel.selectionWorkout()
                        if(select == userData?.version) {
                            navController.navigate("home_screen/1")
                        } else {
                            navController.navigate("start_screen")
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
                text = if(select == userData?.version) "Continue" else "Change plan",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )
        }
    }
}


