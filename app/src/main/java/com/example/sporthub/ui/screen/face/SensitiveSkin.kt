package com.example.sporthub.ui.screen.face

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sporthub.ui.components.baseGlass
import com.example.sporthub.ui.components.face.FaceComposition
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.FaceViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@Composable
fun SensitiveSkin(
    navController: NavController,
    sliderPosition: Float,
    onSliderPosition: (Float) -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite),
                    startY = 0f,
                    endY = 1500f
                )
            )
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Skip",
                color = LightGray,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
                modifier = Modifier
                    .clickable(
                        onClick = { navController.navigate("face_statistics_screen") },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        }

        Spacer(Modifier.height(20.dp))
        Text(
            text = "How sensitive is your skin?",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = "This helps us adjust how ofter powerful actives appear in your routine",
            color = LightGray,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))
        FaceComposition(
            sliderPosition,
            onSliderPosition
        )
    }
}

@Composable
fun SensitiveBottomBar(
    navController: NavHostController,
    faceViewModel: FaceViewModel
) {
    val savedValue by faceViewModel.faceData.observeAsState()

    var sliderPosition by remember { mutableFloatStateOf(30f) }

    LaunchedEffect(savedValue) {
        savedValue?.let {
            sliderPosition = it.sensitive
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        SensitiveSkin(
            navController,
            sliderPosition,
            onSliderPosition = { sliderPosition = it },
            modifier = Modifier.layerBackdrop(backdrop)
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
                    .baseGlass(backdrop)
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
                            faceViewModel.addSensitive(sliderPosition)
                            navController.navigate("face_statistics_screen")
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