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
import com.example.cognilink.data.Answer
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.theme.CogniLinkTheme

@Composable
fun AnswerOptions(
    modifier: Modifier = Modifier,
    answerOptions: List<Answer>, // Esta lista deve vir do seu ViewModel ou Estado Pai
    flashcardType: FlashcardType,
    selectedAnswer: Answer? = null, // Para seleção única (MULTIPLE_CHOICE)
    onSelectedAnswer: (Answer?) -> Unit = {},
    selectedAnswers: List<Answer> = emptyList(), // Para seleção múltipla (TRUE_OR_FALSE)
    onToggleAnswer: (Answer) -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        answerOptions.forEachIndexed { index, response ->
            val isSelected = if (flashcardType == FlashcardType.TRUE_OR_FALSE) {
                selectedAnswers.contains(response)
            } else {
                response == selectedAnswer
            }

            AnswerItem(
                flashcardType = flashcardType,
                label = setLabel(flashcardType, index),
                answerText = response.answer,
                onAnswerTextChange = null,
                selected = isSelected,
                onSelect = {
                    if (flashcardType == FlashcardType.TRUE_OR_FALSE) {
                        onToggleAnswer(response)
                    } else {
                        val newSelection = if (response == selectedAnswer) null else response
                        onSelectedAnswer(newSelection)
                    }
                },
                onClickToRemove = { },
            )
        }
    }
}

@Preview
@Composable
private fun AnswerOptionsPreview() {
    val listaTeste = listOf(
        Answer("Resposta 1", true),
        Answer("Resposta 2", false),
        Answer("Resposta 3", false)
    )

    var selectedAnswer by remember { mutableStateOf<Answer?>(null) }
    var selectedAnswers by remember { mutableStateOf(listOf<Answer>()) }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CogniLinkTheme {
            // Exemplo Múltipla Escolha (Single)
            AnswerOptions(
                flashcardType = FlashcardType.MULTIPLE_CHOICE,
                answerOptions = listaTeste,
                selectedAnswer = selectedAnswer,
                onSelectedAnswer = { selectedAnswer = it }
            )

            // Exemplo Verdadeiro ou Falso (Multiple)
            AnswerOptions(
                flashcardType = FlashcardType.TRUE_OR_FALSE,
                answerOptions = listaTeste,
                selectedAnswers = selectedAnswers,
                onToggleAnswer = { answer ->
                    selectedAnswers = if (selectedAnswers.contains(answer)) {
                        selectedAnswers - answer
                    } else {
                        selectedAnswers + answer
                    }
                }
            )
        }
    }
}