package com.example.cognilink.ui.states

import com.example.cognilink.data.model.Answer
import com.example.cognilink.data.model.Flashcard

data class FlashcardPlayerUiState(
    val currentFlashcard: Flashcard? = null,
    val selectedAnswers: Map<Answer, String> = emptyMap(),
    val isQuestionVerified: Boolean = false
) {
    val isQuestionAnswered: Boolean = selectedAnswers.isNotEmpty()
}
