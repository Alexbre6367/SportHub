package com.example.sporthub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.theme.LightWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray

@Composable
fun DialogScreen(
    emailState: MutableState<String>,
    onDismiss: () -> Unit,
    onResend: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = LightWhite,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                tint = black
            )
        }

        Spacer(Modifier.height(12.dp))
        Text(
            text = "Check your email",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = "We've sent a link to your email:",
            color = gray,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = emailState.value,
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .height(58.dp)
                .fillMaxWidth()
                .clickable(
                    onClick = { onDismiss() },
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

        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .height(58.dp)
                .fillMaxWidth()
                .clickable(
                    onClick = { onDismiss() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                )
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Resend email",
                color = gray,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
                modifier = Modifier.clickable { onResend() },
            )
        }

    }
}