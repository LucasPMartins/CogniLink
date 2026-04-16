package com.example.cognilink.ui.components.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Red
import com.example.cognilink.ui.theme.White

@Composable
fun ResponseItem(
    modifier: Modifier = Modifier,
    flashcardType: FlashcardType,
    label: String, // Ex: "A", "B", "C"
    responseText: String,
    onResponseChange: ((String) -> Unit)? = null, // Se for null, é modo leitura (Estudo)
    checked: Boolean,
    onSelect: () -> Unit,
    onClickToRemove: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        onClick = onSelect,
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        color = Color.White,
        border = if (checked) BorderStroke(2.dp, DarkNavyBlue) else null // Destaque visual
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Seleção de Tipo
            when (flashcardType) {
                FlashcardType.MULTIPLE_CHOICE -> {
                    RadioButton(
                        selected = checked,
                        onClick = onSelect,
                        colors = RadioButtonDefaults.colors(selectedColor = DarkNavyBlue),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
                FlashcardType.TRUE_OR_FALSE -> {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { onSelect() },
                        colors = CheckboxDefaults.colors(checkedColor = DarkNavyBlue),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
                FlashcardType.OMISSION -> {}
                FlashcardType.BASIC -> {}
                FlashcardType.CHAT_FEYNMAN -> {}
            }

            Text(
                text = "$label",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkNavyBlue
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Área de Texto: Decide se é editável ou apenas leitura
            Box(modifier = Modifier.weight(1f)) {
                if (onResponseChange != null) {
                    // MODO EDITOR
                    OutlinedTextField(
                        value = responseText,
                        onValueChange = onResponseChange,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                        placeholder = { Text("Digite a resposta...") }
                    )
                } else {
                    // MODO ESTUDO
                    Text(
                        text = responseText,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            if (onResponseChange != null) {
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
private fun ResponseItemPreview() {
    CogniLinkTheme {
        ResponseItem(
            flashcardType = FlashcardType.MULTIPLE_CHOICE,
            label = "A.",
            responseText = "Resposta",
            onResponseChange = { },
            checked = false,
            onSelect = {},
            onClickToRemove = {},
        )
//        AnswerItem(answerType = "Verdadeiro ou Falso",
//            answer = "A. Verdadeiro",
//            onAnswerChange = {},
//            checked = false,
//            onSelect = {}
//        )
    }
}