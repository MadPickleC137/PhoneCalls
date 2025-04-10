package com.madpickle.calls.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val PaddingItem = 8.dp
val HeightItem = 56.dp
val IconCornersShape = RoundedCornerShape(28.dp)
val CardItemShape = RoundedCornerShape(12.dp)
val FabShape = RoundedCornerShape(16.dp)
val PaddingCardItem = PaddingValues(10.dp)

val ButtonElevation @Composable get() = ButtonDefaults.elevation(
    defaultElevation = 0.dp,
    pressedElevation = 0.dp,
    hoveredElevation = 0.dp,
    focusedElevation = 0.dp
)