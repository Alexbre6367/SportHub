package com.example.sporthub.ui.components.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face3
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.LightPurple
import com.example.sporthub.ui.theme.activityGreenEnd
import com.example.sporthub.ui.theme.activityRedStart
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.viewmodel.FaceViewModel
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

@Composable
fun Widget(
    navController: NavController,
    faceViewModel: FaceViewModel
) {
    val faceData by faceViewModel.faceData.observeAsState()
    val acne = faceData?.acne ?: 0
    val dryness = faceData?.dryness ?: 0
    val moisture = faceData?.moisture ?: 0

    if(faceData?.widget == true) {
        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
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
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .drawBackdrop(
                                backdrop = rememberLayerBackdrop(),
                                shape = { CircleShape },
                                effects = {
                                    vibrancy()
                                    blur(2f.dp.toPx())
                                    lens(16f.dp.toPx(), 32f.dp.toPx())
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Face3,
                            contentDescription = null,
                            tint = activityRedStart
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "$acne%",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = black
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Acne",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 12.sp,
                        color = LightGray
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .drawBackdrop(
                                backdrop = rememberLayerBackdrop(),
                                shape = { CircleShape },
                                effects = {
                                    vibrancy()
                                    blur(2f.dp.toPx())
                                    lens(16f.dp.toPx(), 32f.dp.toPx())
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Water,
                            contentDescription = null,
                            tint = LightPurple
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "$dryness%",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = black
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Dryness",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 12.sp,
                        color = LightGray
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .drawBackdrop(
                                backdrop = rememberLayerBackdrop(),
                                shape = { CircleShape },
                                effects = {
                                    vibrancy()
                                    blur(2f.dp.toPx())
                                    lens(16f.dp.toPx(), 32f.dp.toPx())
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = LightBlue
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "$moisture%",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = black
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Moisture",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 12.sp,
                        color = LightGray
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(140.dp)
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { RoundedCornerShape(24.dp) },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            }
                        )
                        .clickable(
                            onClick = { navController.navigate("face_statistics_screen") },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.AddAPhoto,
                        contentDescription = null,
                        tint = activityGreenEnd,
                        modifier = Modifier.size(68.dp)
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "New Scan",
                        style = MaterialTheme.typography.titleLarge,
                        color = LightGray,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}