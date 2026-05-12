package com.example.cognilink.ui.components.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.White

@Composable
fun HintItem(
    modifier: Modifier = Modifier,
    label: String,
    hint: String,
    onHintChange: ((String) -> Unit)? = null, // Se for null, é modo leitura (Estudo)
    onClickToRemove: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, LightGray),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = DarkNavyBlue,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)
            )
            // Área de Texto: Decide se é editável ou apenas leitura
            Box(modifier = Modifier.weight(1f)) {
                if (onHintChange != null) {
                    // MODO EDITOR
                    OutlinedTextField(
                        value = hint,
                        onValueChange = onHintChange,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            ),
                        placeholder = { Text("Digite a dica...") }
                    )
                } else {
                    // MODO VISUAL
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkGray,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
            if (onHintChange != null) {
                IconButton(onClick = onClickToRemove) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_remove), // Use ícones padrão do Material se possível
                        contentDescription = "Remover",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HintItemPreview() {
    var hint by remember { mutableStateOf("Teste") }

    HintItem(label = "Dica 1", hint = hint, onHintChange = { hint = it }, onClickToRemove = {})
}