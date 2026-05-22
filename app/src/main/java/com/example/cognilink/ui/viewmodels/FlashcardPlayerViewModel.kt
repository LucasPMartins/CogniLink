package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cognilink.data.model.Answer
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.ui.states.FlashcardPlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update



class FlashcardPlayerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardPlayerUiState())
    val uiState: StateFlow<FlashcardPlayerUiState> = _uiState.asStateFlow()

    fun onSelectAnswer(answer: Answer, choice: String = "") {
        _uiState.update { currentState ->
            if (currentState.isQuestionVerified) return@update currentState

            val newAnswers = when (currentState.currentFlashcard?.cardType) {
                FlashcardType.MULTIPLE_CHOICE, FlashcardType.BASIC -> mapOf(answer to choice)
                else -> currentState.selectedAnswers + (answer to choice)
            }
            currentState.copy(selectedAnswers = newAnswers)
        }
    }

    fun nextFlashcard(nextFlashcard: Flashcard) {
        _uiState.update {
            it.copy(
                currentFlashcard = nextFlashcard,
                selectedAnswers = emptyMap(),
                isQuestionVerified = false
            )
        }
    }

    fun verifyQuestion() {
        _uiState.update { it.copy(isQuestionVerified = !it.isQuestionVerified) }
    }

    fun saveFlashcard() {
        // Implement save logic using a repository
    }
}
