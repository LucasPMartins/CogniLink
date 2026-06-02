package com.example.cognilink.ui.states

import com.example.cognilink.data.model.Answer
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.domain.model.FlashcardType
import java.util.UUID

data class FlashcardEditorUiState(
    val deckId: String? = null,
    val flashcardId: String = UUID.randomUUID().toString(),
    val questionText: String = "",
    val cardType: FlashcardType = FlashcardType.BASIC,
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val answerOptions: List<Answer> = emptyList(),
    val hints: List<String> = emptyList(),
    val isDeleteMode: Boolean = false,
    val isInitialized: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
