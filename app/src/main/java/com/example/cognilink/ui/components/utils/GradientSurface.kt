package com.example.cognilink.ui.components.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun GradientSurface(
    modifier: Modifier = Modifier,
    initialColor: Color = Color(0xFF000666),
    finalColor: Color = Color(0xFF1222B0),
    startOffset: Offset = Offset.Zero,
    endOffset: Offset = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    shape: Shape = RectangleShape,
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    shadowElevation: Int = 0,
    content: @Composable () -> Unit,
    ) {
    Surface(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        initialColor,
                        finalColor
                    ),
                    // Opcional: define a direção (padrão é da esquerda-topo para direita-baixo)
                    start = startOffset,
                    end = endOffset
                ),
                shape = shape
            ),
        color = Color.Transparent,
        shape = shape,
        shadowElevation = shadowElevation.dp,
        border = border
    ){
        content()
    }
}