package com.example.sporthub.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.sporthub.ui.theme.OffWhite
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

fun Modifier.baseGlass(backdrop: Backdrop, blur: Dp = 2f.dp,  shape: Shape = CircleShape, effects: Boolean = true, drawRect: Boolean = false) : Modifier = this
    .drawBackdrop(
        backdrop = backdrop,
        shape = { shape },
        effects = {
            if(effects) {
                vibrancy()
                blur(blur.toPx())
                lens(16f.dp.toPx(), 32f.dp.toPx())
            }
        },
        onDrawSurface = {
            if(drawRect) {
                drawRect(OffWhite, alpha = 0.6f, blendMode = BlendMode.Overlay)
                drawRect(OffWhite.copy(alpha = 0.7f))
            }
        }
    )