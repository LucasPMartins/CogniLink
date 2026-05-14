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
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.theme.neonGlow

@Composable
fun CustomButton(modifier: Modifier = Modifier,
                 text: String = "ADICIONAR FLASHCARD",
                 icon: Int = R.drawable.ic_add,
                 onClickButton: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
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
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClickButton),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
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
            Text(
                text = text,
                color = White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}

@Preview
@Composable
private fun CustomButtomPreview() {
    CustomButton() { }
    
}