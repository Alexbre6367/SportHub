package com.example.sporthub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.ChatMessage

@Composable
fun MessageBox(message: ChatMessage) {
    val padding = if(message.isUser) PaddingValues(start = 20.dp) else PaddingValues(end = 20.dp)
    val modifier = Modifier
        .padding(padding)
        .defaultMinSize(minHeight = 58.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(Color.White)
        .padding(16.dp)


    val boxArrangement = if(message.isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        contentAlignment = boxArrangement
    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = message.text,
                fontSize = 18.sp,
                color = LightGray,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
