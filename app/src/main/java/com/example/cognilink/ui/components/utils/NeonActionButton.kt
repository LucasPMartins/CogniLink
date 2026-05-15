package com.example.cognilink.ui.components.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Gray
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.theme.neonGlow

@Composable
fun NeonActionButton(
    modifier: Modifier = Modifier,
    text: String = "ADICIONAR FLASHCARD",
    height: Dp = 70.dp,
    icon: Int? = null,
    iconRightSide: Boolean = false,
    onClickButton: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF000666), // Cor inicial
                        Color(0xFF1222B0)  // Cor final
                    ),
                    // Opcional: define a direção (padrão é da esquerda-topo para direita-baixo)
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(26.dp)
            ),
        color = Color.Transparent,
        shape = RoundedCornerShape(26.dp),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClickButton),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null && !iconRightSide) {
                Surface(
                    shape = CircleShape,
                    color = White,
                    modifier = Modifier.neonGlow(White, 24.dp, 5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = DarkNavyBlue,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            Text(
                text = text,
                color = White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp)
            )
            if (icon != null && iconRightSide) {
                Surface(
                    shape = CircleShape,
                    color = White,
                    modifier = Modifier.neonGlow(White, 24.dp, 5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = DarkNavyBlue,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleGradientButton(
    modifier: Modifier = Modifier,
    text: String = "ADICIONAR FLASHCARD",
    height: Dp = 70.dp,
    icon: Int? = null,
    iconColor: Color = White,
    iconRightSide: Boolean = false,
    isEnabled: Boolean = true,
    onClickButton: () -> Unit,
) {
    val initalColor = if (isEnabled) Color(0xFF000666) else Gray
    val finalColor = if (isEnabled) Color(0xFF1222B0) else OffWhite

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(initalColor, finalColor),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(26.dp)
            ),
        color = Color.Transparent,
        shape = RoundedCornerShape(26.dp),
        shadowElevation = if(isEnabled) 2.dp else 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClickButton),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null && !iconRightSide) {

                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = iconColor,
                )

            }
            Text(
                text = text,
                color = White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp)
            )
            if (icon != null && iconRightSide) {

                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = iconColor,
                )

            }
        }
    }
}

@Preview
@Composable
private fun ActionButtomPreview() {
    //NeonActionButton(icon = R.drawable.ic_arrow_forward, iconRightSide = true) { }
    SimpleGradientButton(icon = R.drawable.ic_arrow_forward, iconRightSide = true, isEnabled = true) { }

}