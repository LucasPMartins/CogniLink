package com.example.cognilink.ui.states

import com.example.cognilink.data.model.Answer
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.domain.model.FlashcardType

data class FlashcardEditorUiState(
    val questionText: String = "",
    val cardType: FlashcardType = FlashcardType.BASIC,
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val answerOptions: List<Answer> = emptyList(),
    val hints: List<String> = emptyList(),
    val isDeleteMode: Boolean = false,
    val currentFlashcardId: Long = 0L,
    val isInitialized: Boolean = false
)
