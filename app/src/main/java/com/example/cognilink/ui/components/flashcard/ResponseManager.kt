package com.example.cognilink.ui.components.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.utils.LabeledText
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.VividCyan
import com.example.cognilink.ui.theme.White
import org.w3c.dom.Text

fun setLabel(flashcardType: FlashcardType, index: Int): String {
    return when (flashcardType) {
        FlashcardType.MULTIPLE_CHOICE -> {
            val letters = listOf("A", "B", "C", "D", "E")
            letters.getOrNull(index)?.let { "$it." } ?: ""
        }
        FlashcardType.OMISSION -> "$${index + 1}$"
        else -> ""
    }
}

@Composable
fun ResponseManager(
    modifier: Modifier = Modifier,
    responses: List<String>, // Esta lista deve vir do seu ViewModel ou Estado Pai
    onResponsesUpdate: (List<String>) -> Unit, // Callback para atualizar a lista toda
    flashcardType: FlashcardType,
    onSelectedAnswer: (Int) -> Unit, // Passa o index da selecionada
    limit: Int = 5
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        responses.forEachIndexed { index, responseText ->
            ResponseItem(
                flashcardType = flashcardType,
                label = setLabel(flashcardType, index),
                responseText = responseText,
                onResponseChange = { newValue ->
                    val newList = responses.toMutableList()
                    newList[index] = newValue
                    onResponsesUpdate(newList)
                },
                checked = false, // Aqui você precisará de um estado para 'selectedIndex'
                onSelect = { onSelectedAnswer(index) },
                onClickToRemove = {
                    val newList = responses.toMutableList()
                    newList.removeAt(index)
                    onResponsesUpdate(newList)
                },
            )
        }

        if (responses.size < limit) {
            OutlinedButton(
                onClick = {
                    val newList = responses.toMutableList()
                    newList.add("")
                    onResponsesUpdate(newList)
                },
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = White,
                    containerColor = DarkNavyBlue
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = VividCyan
                )
                Spacer(Modifier.width(8.dp))
                Text("Adicionar opção de resposta (${responses.size + 1}/$limit)")
            }
        }
    }
}

@Preview
@Composable
private fun ResponseManagerPreview() {
    //var listaTeste by remember { mutableStateOf(emptyList<String>()) }
    var listaTeste by remember { mutableStateOf(listOf("Resposta 1", "Resposta 2", "Resposta 3")) }
    CogniLinkTheme {
        ResponseManager(
            flashcardType = FlashcardType.OMISSION,
            responses = listaTeste,
            onResponsesUpdate = { listaTeste = it },
            onSelectedAnswer = {},
        )
    }
}