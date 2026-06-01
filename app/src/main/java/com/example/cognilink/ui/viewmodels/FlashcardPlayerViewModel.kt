package com.example.cognilink.ui.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.Answer
import com.example.cognilink.data.repository.FlashcardRepository
import com.example.cognilink.data.repository.FlashcardRepositoryImpl
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.ui.states.FlashcardPlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FlashcardPlayerViewModel(
    private val repository: FlashcardRepository = FlashcardRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardPlayerUiState())
    val uiState: StateFlow<FlashcardPlayerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _uiState.update { it.copy(secondsElapsed = it.secondsElapsed + 1) }
            }
        }
    }

    fun initializeSession(studyMode: String, contextId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val flashcards = when (studyMode) {
                "DECK" -> repository.getFlashcardsForDeck(contextId)
                "LEECHES" -> repository.getLeeches(contextId) ?: emptyList()
                "REVIEW" -> repository.getReviewPending(contextId) ?: emptyList()
                "FLASHCARD" -> repository.getFlashcardById(contextId)?.let { listOf(it) }
                    ?: emptyList()

                else -> emptyList()
            }
            val sessionTitle = when (studyMode) {
                "DECK" -> repository.getDeckName(contextId) ?: "ESTUDAR DECK"
                "LEECHES" -> "SESSÃO DE LEECHES"
                "REVIEW" -> "REVISÃO"
                "FLASHCARD" -> flashcards.firstOrNull()?.deckId?.let { repository.getDeckName(it) } ?: "ESTUDAR FLASHCARD"
                else -> ""
            }

            if (flashcards.isEmpty()) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            _uiState.update {
                it.copy(
                    sessionFlashcards = flashcards,
                    sessionTitle = sessionTitle,
                    currentFlashcardIndex = 0,
                    isLoading = false,
                    isQuestionVerified = false,
                    selectedAnswers = emptyMap()
                )
            }
        }
    }

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

    fun nextFlashcard() {
        if (_uiState.value.isLastFlashcard) {
            _uiState.update {
                it.copy(
                    isSessionInsightDialogOpen = true
                )
            }
        } else {
            _uiState.update {
                val nextIndex = it.currentFlashcardIndex + 1
                it.copy(
                    currentFlashcardIndex = nextIndex,
                    selectedAnswers = emptyMap(),
                    isQuestionVerified = false
                )
            }
        }
    }

    fun verifyQuestion() {
        val currentState = _uiState.value
        val currentFlashcard = currentState.currentFlashcard ?: return

        val isCorrect = when (currentFlashcard.cardType) {
            FlashcardType.BASIC -> {
                val userAnswer = currentState.selectedAnswers.values.firstOrNull()?.trim()
                val correctAnswer = currentFlashcard.answerOptions.firstOrNull()?.answer?.trim()
                userAnswer.equals(correctAnswer, ignoreCase = true)
            }

            FlashcardType.MULTIPLE_CHOICE -> {
                currentState.selectedAnswers.keys.any { it.isCorrect }
            }

            FlashcardType.TRUE_OR_FALSE -> {
                currentState.selectedAnswers.all { (answer, choice) ->
                    (choice == "T" && answer.isCorrect) || (choice == "F" && !answer.isCorrect)
                }
            }

            else -> true
        }

        _uiState.update {
            it.copy(
                isQuestionVerified = true,
                sequenceHits = if (isCorrect) it.sequenceHits + 1 else 0
            )
        }
    }

    fun toggleSessionInsightDialog() {
        _uiState.update { it.copy(isSessionInsightDialogOpen = !it.isSessionInsightDialogOpen) }
    }


    fun saveFlashcard() {
        _uiState.value.currentFlashcard?.let { flashcard ->
            viewModelScope.launch {
                repository.saveFlashcard(flashcard)
            }
        }
    }

    fun toggleCloseDialog() {
        _uiState.update { it.copy(isCloseDialogOpen = !it.isCloseDialogOpen) }
    }

    @SuppressLint("DefaultLocale")
    fun formatSeconds(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        if (h == 0L) return String.format("%02d:%02d", m, s)
        return String.format("%02d:%02d:%02d", h, m, s)
    }
}
