package com.example.sporthub.ui.screen.workout

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.components.BottomBarContinue
import com.example.sporthub.ui.components.Free
import com.example.sporthub.ui.components.Pro
import com.example.sporthub.ui.components.baseGlass
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@Composable
fun SelectionScreen(select: Int) {
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
    }
}

@Composable
fun SelectionTopBar(
    modifier: Modifier = Modifier,
    select: Int,
    onSelectChange: (Int) -> Unit
) {
    val value by animateFloatAsState(
        targetValue = if(select == 1) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(Modifier.fillMaxSize()) {
        SelectionScreen(select)

        Box(
            modifier = modifier
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
                    .baseGlass(rememberLayerBackdrop(), effects = false)
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
                        ) { onSelectChange(1) },
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
                        ) { onSelectChange(2) },
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
    loginViewModel: LoginViewModel
) {
    val userData by loginViewModel.currentUser.collectAsState()
    var select by remember { mutableIntStateOf(userData?.version ?: 2) }

    Box(Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        SelectionTopBar(
            modifier = Modifier.layerBackdrop(backdrop),
            select = select,
            onSelectChange = { select = it }
        )

        val userData by loginViewModel.currentUser.collectAsState()

        BottomBarContinue(
            backdrop,
            navController = navController,
            onClick = {
                loginViewModel.selectionWorkout()
                if (select == userData?.version) {
                    navController.navigate("home_screen/1")
                } else {
                    navController.navigate("start_screen")
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            text = if (select == userData?.version) "Continue" else "Change plan"
        )
    }
}