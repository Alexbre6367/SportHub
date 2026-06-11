package com.example.sporthub.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.screen.workout.selectionCardStyle
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.activityRedEnd
import com.example.sporthub.ui.theme.appleOrange
import com.example.sporthub.ui.theme.applePink
import com.example.sporthub.ui.theme.applePurple
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.colorError
import com.example.sporthub.ui.theme.googleGreen
import com.example.sporthub.ui.theme.googleYellow
import com.example.sporthub.ui.theme.ringColor
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

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
                        .baseGlass(rememberLayerBackdrop()),
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
                        .baseGlass(rememberLayerBackdrop()),
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
                    .baseGlass(rememberLayerBackdrop(), shape = RoundedCornerShape(24.dp))
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
                        .baseGlass(rememberLayerBackdrop(), shape = RoundedCornerShape(24.dp))
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
                        .baseGlass(rememberLayerBackdrop()),
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
                        .baseGlass(rememberLayerBackdrop()),
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