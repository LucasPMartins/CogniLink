package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.Answer
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.repository.FlashcardRepository
import com.example.cognilink.data.repository.FlashcardRepositoryImpl
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.ui.states.FlashcardEditorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashcardEditorViewModel(
    private val repository: FlashcardRepository = FlashcardRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardEditorUiState())
    val uiState: StateFlow<FlashcardEditorUiState> = _uiState.asStateFlow()

    fun initialize(deckId: String, flashcardId: String? = null) {
        if (_uiState.value.deckId == deckId && _uiState.value.flashcardId == flashcardId) return
        _uiState.update { currentState ->
            currentState.copy(
                deckId = deckId,
                flashcardId = flashcardId ?: currentState.flashcardId
            )
        }
        loadFlashcard()
    }

    private fun loadFlashcard() {
        val currentState = _uiState.value
        viewModelScope.launch {
            try {
                val flashcard = repository.getFlashcardById(currentState.flashcardId)
                if (flashcard != null) {
                    _uiState.update { currentState ->
                        if (currentState.isInitialized) return@update currentState

                        currentState.copy(
                            flashcardId = flashcard.id,
                            questionText = flashcard.question,
                            cardType = flashcard.cardType,
                            difficulty = flashcard.difficulty,
                            answerOptions = flashcard.answerOptions,
                            hints = flashcard.hints,
                            isInitialized = true
                        )

                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onBasicAnswerChange(newAnswerText: String) {
        _uiState.update {
            it.copy(
                answerOptions = listOf(
                    Answer(
                        answer = newAnswerText,
                        isCorrect = true
                    )
                )
            )
        }
    }

    fun removeAnswer(answer: Answer) {
        _uiState.update { it.copy(answerOptions = it.answerOptions.filter { a -> a != answer }) }
    }

    fun selectCorrectAnswer(selectedIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                answerOptions = currentState.answerOptions.mapIndexed { index, answer ->
                    answer.copy(isCorrect = index == selectedIndex)
                }
            )
        }
    }

    fun toggleTrueFalseAnswer(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                answerOptions = currentState.answerOptions.mapIndexed { i, a ->
                    if (i == index) a.copy(isCorrect = !a.isCorrect)
                    else a
                }
            )
        }
    }

    fun toggleDeletionMode() {
        _uiState.update { it.copy(isDeleteMode = !it.isDeleteMode) }
    }

    fun onQuestionTextChange(newQuestion: String) {
        _uiState.update { it.copy(questionText = newQuestion) }
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel) {
        _uiState.update { it.copy(difficulty = newDifficulty) }
    }

    fun updateAnswers(newAnswers: List<Answer>) {
        _uiState.update { it.copy(answerOptions = newAnswers) }
    }

    fun updateHints(newHints: List<String>) {
        _uiState.update { it.copy(hints = newHints) }
    }

    fun onTypeChange(newType: FlashcardType) {
        _uiState.update { it.copy(cardType = newType, answerOptions = emptyList()) }
    }

    fun saveFlashcard() {
        val currentState = _uiState.value
        val deckId = currentState.deckId ?: return

        val flashcardToSave = Flashcard(
            id = currentState.flashcardId,
            question = currentState.questionText,
            cardType = currentState.cardType,
            difficulty = currentState.difficulty,
            answerOptions = currentState.answerOptions,
            hints = currentState.hints,
            deckId = deckId
        )
        viewModelScope.launch {
            repository.saveFlashcard(flashcardToSave)
        }
    }
}
