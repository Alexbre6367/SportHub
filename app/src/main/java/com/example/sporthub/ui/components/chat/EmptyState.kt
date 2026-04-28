package com.example.sporthub.ui.components.chat

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.theme.appleBlue
import com.example.sporthub.ui.theme.appleOrange
import com.example.sporthub.ui.theme.applePink
import com.example.sporthub.ui.theme.applePurple
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.GeminiViewModel
import com.example.sporthub.ui.viewmodel.LoginViewModel

@Composable
fun EmptyStateGemini(
    loginViewModel: LoginViewModel,
    geminiViewModel: GeminiViewModel
) {
    val isLoading by geminiViewModel.isLoading.collectAsState()
    val userData = loginViewModel.currentUser.collectAsState()
    val boxModifier = Modifier
        .height(46.dp)
        .border(
            1.dp,
            gray,
            shape = CircleShape
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { translationY = -size.height * 0.15f },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Hello, ${userData.value?.name}",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            appleBlue,
                            applePurple,
                            applePink,
                            appleOrange
                        )
                    )
                ),
                fontSize = 36.sp
            )
            Text(
                text = "How can I help you today?",
                color = gray,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp
            )
        }

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = boxModifier
                    .weight(1f)
                    .clickable(
                        enabled = !isLoading,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        geminiViewModel.message("Все показатели здоровья пользователя")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Health",
                    color = gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.width(12.dp))
            Box(
                modifier = boxModifier
                    .weight(1f)
                    .clickable(
                        enabled = !isLoading,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        geminiViewModel.message("План пиатния на день")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nutrition",
                    color = gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.width(12.dp))
            Box(
                modifier = boxModifier
                    .weight(1f)
                    .clickable(
                        enabled = !isLoading,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        geminiViewModel.message("Легкая тренировка")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Workout",
                    color = gray,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            }
        }
    }
}
