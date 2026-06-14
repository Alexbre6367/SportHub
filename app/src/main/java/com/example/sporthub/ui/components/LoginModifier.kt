package com.example.sporthub.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sporthub.ui.theme.backgroundGradient

@Composable
fun Modifier.mainColumn(
    scrollState: ScrollState = rememberScrollState(),
    onClick: () -> Unit = {},
    paddingTop: Boolean = true
): Modifier = this
    .fillMaxSize()
    .background(backgroundGradient)
    .verticalScroll(scrollState)
    .clickable(
        onClick = onClick,
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    )
    .statusBarsPadding()
    .padding(horizontal = 20.dp)
    .padding(top = if(paddingTop) 70.dp else 0.dp)
