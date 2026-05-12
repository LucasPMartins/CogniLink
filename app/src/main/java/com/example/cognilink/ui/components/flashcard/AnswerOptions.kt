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

//TODO: criar classe para resposta
data class AnswerOption(val text: String, val isCorrect: Boolean)

@Composable
fun AnswerOptions(
    modifier: Modifier = Modifier,
    responses: List<AnswerOption>, // Esta lista deve vir do seu ViewModel ou Estado Pai
    flashcardType: FlashcardType,
    selectedAnswer: AnswerOption,
    onSelectedAnswer: (AnswerOption) -> Unit, // Passa o index da selecionada
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        responses.forEachIndexed { index, response ->
            AnswerItem(
                flashcardType = flashcardType,
                label = setLabel(flashcardType, index),
                responseText = response.text,
                onResponseChange = null,
                selected = response.text == selectedAnswer.text,
                onSelect = { onSelectedAnswer(response) },
                onClickToRemove = { },
            )
        }
    }
}

@Preview
@Composable
private fun AnswerOptionsPreview() {
    //var listaTeste by remember { mutableStateOf(emptyList<String>()) }
    var listaTeste by remember { mutableStateOf(listOf(AnswerOption("Resposta 1", true), AnswerOption("Resposta 2", false), AnswerOption("Resposta 3", false))) }

    var selectedAnswer by remember { mutableStateOf(listaTeste[0]) }

    CogniLinkTheme {
        AnswerOptions(
            flashcardType = FlashcardType.MULTIPLE_CHOICE,
            responses = listaTeste,
            selectedAnswer = selectedAnswer,
            onSelectedAnswer = { response -> selectedAnswer = response }
        )
    }
}