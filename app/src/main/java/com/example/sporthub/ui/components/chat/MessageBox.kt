package com.example.sporthub.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.viewmodel.ChatMessage

@Composable
fun MessageBox(message: ChatMessage) {
    val padding = if (message.isUser) PaddingValues(start = 20.dp) else PaddingValues(end = 20.dp)
    val modifier = Modifier
        .padding(padding)
        .clip(RoundedCornerShape(24.dp))
        .background(Color.White)
        .padding(16.dp)


    val alignment = if (message.isUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = alignment
    ) {
        message.image?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(116.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
        }

        if(message.text.isNotBlank()) {
            Box(modifier = modifier) {
                Text(
                    text = message.text,
                    fontSize = 18.sp,
                    color = LightGray,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
