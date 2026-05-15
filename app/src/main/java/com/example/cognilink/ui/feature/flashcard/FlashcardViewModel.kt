package com.example.cognilink.ui.feature.flashcard

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cognilink.data.Answer
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashcardType

class FlashcardViewModel : ViewModel() {

    var question by mutableStateOf("")
        private set

    var cardType by mutableStateOf(FlashcardType.BASIC)
        private set

    var difficulty by mutableStateOf(DifficultyLevel.MEDIUM)
        private set

    var answers by mutableStateOf(listOf<Answer>())
        private set

    var hints by mutableStateOf(listOf<String>())
        private set

    fun onQuestionChange(newQuestion: String) {
        question = newQuestion
    }

    fun onTypeChange(newType: FlashcardType) {
        cardType = newType
        // Ajusta as respostas baseado no novo tipo
        answers = when (newType) {
            FlashcardType.BASIC -> listOf(Answer("", true))
            FlashcardType.TRUE_OR_FALSE -> listOf(
                Answer("Verdadeiro", true),
                Answer("Falso", false)
            )
            FlashcardType.MULTIPLE_CHOICE -> listOf(
                Answer("", true),
                Answer("", false),
                Answer("", false),
                Answer("", false)
            )
            FlashcardType.OMISSION -> listOf(Answer("", true))
            FlashcardType.CHAT_FEYNMAN -> listOf(Answer("", true))
        }
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel) {
        difficulty = newDifficulty
    }

    fun updateAnswers(newAnswers: List<Answer>) {
        answers = newAnswers
    }

    fun onSelectedAnswer(index: Int) {
        answers = answers.mapIndexed { i, answer ->
            if (cardType == FlashcardType.TRUE_OR_FALSE) {
                if (i == index) answer.copy(isCorrect = !answer.isCorrect) else answer
            } else {
                answer.copy(isCorrect = i == index)
            }
        }
    }

    fun updateHints(newHints: List<String>) {
        hints = newHints
    }
    
    fun onBasicAnswerChange(newAnswer: String) {
        if (cardType == FlashcardType.BASIC) {
            answers = listOf(Answer(newAnswer, true))
        }
    }

    fun saveFlashcard() {
        // Implement save logic using a repository
    }
}
