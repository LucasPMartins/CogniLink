package com.example.cognilink.ui.states

import com.example.cognilink.data.model.Deck
import com.example.cognilink.data.model.Flashcard

data class DeckUiState(
    val currentDeck: Deck? = null,
    val flashcards: List<Flashcard> = emptyList(),
    val isMenuExpanded: Boolean = false,
    val isAddFlashcardDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
