package com.example.sporthub.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporthub.ui.components.baseGlass
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.activityRedEnd
import com.example.sporthub.ui.theme.black
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

@Composable
fun Week(
    days: String,
    isActive: Boolean,
    activeColor: Color = activityRedEnd,
    size: Dp = 24.dp,
    strikeDay: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (strikeDay) {
            Text(
                text = days,
                color = if(isActive) black else LightGray,
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(4.dp))
        }

        Box(
            modifier = Modifier
                .baseGlass(rememberLayerBackdrop())
                .size(size)
                .clip(CircleShape)
                .background(if (isActive) activeColor else Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(size / 2)
                )
            }
        }
        if (!strikeDay) {
            Text(
                text = days,
                color = LightGray,
                fontSize = 12.sp,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}