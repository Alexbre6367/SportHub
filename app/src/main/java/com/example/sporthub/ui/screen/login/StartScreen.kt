package com.example.sporthub.ui.screen.login

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel

@Composable
fun StartScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    var selectedIndex by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite),
                    startY = 0f,
                    endY = 1500f
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 28.dp)
            .padding(top = 38.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Yours one-time offer",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Unlock the most powerful daily AI",
            color = gray,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )
        Spacer(Modifier.height(38.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Fastfood,
                contentDescription = null,
                tint = gray,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(18.dp)
                    .size(52.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(18.dp)
            ) {
                Text(
                    "Easy food tracking",
                    color = black,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    "We help maintain a balanced diet",
                    color = gray,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Watch,
                contentDescription = null,
                tint = gray,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(18.dp)
                    .size(52.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(18.dp)
            ) {
                Text(
                    "Heart rate monitoring",
                    color = black,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    "Comprehensive reports from your watch",
                    color = gray,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Lightbulb,
                contentDescription = null,
                tint = gray,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(18.dp)
                    .size(52.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(18.dp)
            ) {
                Text(
                    "Personalized daily recommendations",
                    color = black,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    "Actionable advice to stay on track",
                    color = gray,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Choose your plan",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp
        )
        Text(
            text = "Выбор ни на что не влияет",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            color = black,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 8.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                    tint = if(selectedIndex == 1) black else gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = "Pro yearly",
                    color = if(selectedIndex == 1) black else gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if(selectedIndex == 1) black else gray,
                            )
                        ) {
                            append("$5.99")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontSize = 14.sp,
                                color = gray
                            )
                        ) {
                            append("/mo")
                        }
                    },
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = "First 7 day free",
                    color = if(selectedIndex == 1) black else gray,
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
                    tint = if(selectedIndex == 2) black else gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = "Free version",
                    color = if(selectedIndex == 2) black else gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if(selectedIndex == 2) black else gray,
                            )
                        ) {
                            append("$0.00")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontSize = 14.sp,
                                color = gray,
                            )
                        ) {
                            append("/mo")
                        }
                    },
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = "First ∞ day free",
                    color = if(selectedIndex == 2) black else gray,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .clickable(
                        onClick = {
                            navController.popBackStack()
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
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f)
                    .padding(start = 12.dp)
                    .clickable(
                        onClick = {
                            loginViewModel.version(selectedIndex)
                            loginViewModel.loadUserData()
                            navController.navigate("home_screen")
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