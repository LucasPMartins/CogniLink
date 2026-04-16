package com.example.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.theme.CogniLinkTheme


@Composable
fun ResponseDisplay(
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
                onResponseChange = null,
                checked = false, // Aqui você precisará de um estado para 'selectedIndex'
                onSelect = { onSelectedAnswer(index) },
                onClickToRemove = { },
            )
        }
    }
}

@Preview
@Composable
private fun ResponseDisplayPreview() {
    //var listaTeste by remember { mutableStateOf(emptyList<String>()) }
    var listaTeste by remember { mutableStateOf(listOf("Resposta 1", "Resposta 2", "Resposta 3")) }
    CogniLinkTheme {
        ResponseDisplay(
            flashcardType = FlashcardType.MULTIPLE_CHOICE,
            responses = listaTeste,
            onResponsesUpdate = { listaTeste = it },
            onSelectedAnswer = {},
        )
    }
}