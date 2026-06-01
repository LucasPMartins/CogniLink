package com.example.cognilink.ui.states

import com.example.cognilink.data.model.Flashcard

data class DeckEditorUiState(
    val deckName: String = "",
    val deckDescription: String = "",
    val deckCategories: List<String> = emptyList(),
    val deckFlashcards: List<Flashcard> = emptyList(),
    val showCategoryDialog: Boolean = false,
    val categoryBeingEdited: String? = null,
    val categoryText: String = "",
    val isRemoveMode: Boolean = false,
    val isAddFlashcardDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)
