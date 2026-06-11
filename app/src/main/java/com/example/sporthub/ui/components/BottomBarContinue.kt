package com.example.sporthub.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.kyant.backdrop.Backdrop


@Composable
fun BottomBarContinue(
    backdrop: Backdrop,
    navController: NavController,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enable: Boolean = !isLoading,
    text: String = "Continue"
) {
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .baseGlass(backdrop)
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
                .background(
                    color = black, shape = CircleShape
                )
                .clickable(
                    enabled = enable,
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = isLoading,
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
                        text = text,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}