package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.Deck
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.repository.DeckRepository
import com.example.cognilink.data.repository.DeckRepositoryImpl
import com.example.cognilink.data.repository.FlashcardRepository
import com.example.cognilink.data.repository.FlashcardRepositoryImpl
import com.example.cognilink.data.repository.UserRepository
import com.example.cognilink.data.repository.UserRepositoryImpl
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.ui.states.DeckEditorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckEditorViewModel(
    private val deckRepository: DeckRepository = DeckRepositoryImpl(),
    private val flashcardRepository: FlashcardRepository = FlashcardRepositoryImpl()
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeckEditorUiState())
    val uiState: StateFlow<DeckEditorUiState> = _uiState.asStateFlow()

    fun toggleRemoveMode() {
        _uiState.update { it.copy(isRemoveMode = !it.isRemoveMode) }
    }

    fun addFlashcard(flashcard: Flashcard) {
        _uiState.update { it.copy(deckFlashcards = it.deckFlashcards + flashcard) }
    }

    fun removeFlashcard(flashcardId: Long) {
        _uiState.update { it.copy(deckFlashcards = it.deckFlashcards.filter { it.id != flashcardId }) }
    }

    fun updateFlashcard(oldFlashcard: Flashcard, newFlashcard: Flashcard) {
        _uiState.update { state ->
            state.copy(
                deckFlashcards = state.deckFlashcards.map { if (it == oldFlashcard) newFlashcard else it }
            )
        }
    }

    fun onDeckNameChange(newValue: String) {
        _uiState.update { it.copy(deckName = newValue) }
    }

    fun onDeckDescriptionChange(newValue: String) {
        _uiState.update { it.copy(deckDescription = newValue) }
    }

    fun onCategoryTextChange(newText: String) {
        _uiState.update { it.copy(categoryText = newText) }
    }

    fun openCategoryDialog(category: String? = null) {
        _uiState.update {
            it.copy(
                categoryBeingEdited = category,
                categoryText = category ?: "",
                showCategoryDialog = true
            )
        }
    }

    fun closeCategoryDialog() {
        _uiState.update {
            it.copy(
                showCategoryDialog = false,
                categoryText = "",
                categoryBeingEdited = null
            )
        }
    }

    fun handleCategoryConfirmation() {
        val currentState = _uiState.value
        if (currentState.categoryText.isNotBlank()) {
            val oldName = currentState.categoryBeingEdited
            val newCategories = if (oldName == null) {
                if (!currentState.deckCategories.contains(currentState.categoryText)) {
                    currentState.deckCategories + currentState.categoryText
                } else {
                    currentState.deckCategories
                }
            } else {
                currentState.deckCategories.map { if (it == oldName) currentState.categoryText else it }
            }
            _uiState.update {
                it.copy(
                    deckCategories = newCategories,
                    showCategoryDialog = false,
                    categoryText = "",
                    categoryBeingEdited = null
                )
            }
        }
    }

    fun removeCategory(category: String) {
        _uiState.update { it.copy(deckCategories = it.deckCategories - category) }
    }

    fun saveDeck(userId: Long, deckId: Long? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val currentState = _uiState.value
                val deckToSave = Deck(
                    id = deckId ?: 0,
                    userId = userId,
                    name = currentState.deckName,
                    description = currentState.deckDescription,
                    categories = currentState.deckCategories,
                    difficulty = DifficultyLevel.EASY, //TODO: Se deckFlashcards não for nulo: Calcular a média de dificuldade dos flashcards
                    mastery = 0f,
                    totalCards = currentState.deckFlashcards.size,
                    cardsToReview = currentState.deckFlashcards.size
                )
                val savedDeckId = deckRepository.saveDeck(deckToSave, userId)
                if (currentState.deckFlashcards.isNotEmpty()) {
                    flashcardRepository.saveAllFlashcards(currentState.deckFlashcards.map {
                        it.copy(
                            deckId = savedDeckId
                        )
                    })
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaved = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun clearEvents() {
        _uiState.update { it.copy(isSaved = false, errorMessage = null) }
    }

    fun toggleAddFlashcardDialog(){
        _uiState.update { it.copy(isAddFlashcardDialogOpen = !it.isAddFlashcardDialogOpen) }
    }

    fun loadDeckData(userId: Long, deckId: Long? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                if (deckId != null) {
                    val deck = deckRepository.getDeckById(deckId, userId)
                    val flashcards = flashcardRepository.getFlashcardsForDeck(deckId)
                    _uiState.update {
                        it.copy(
                            deckName = deck?.name ?: "",
                            deckDescription = deck?.description ?: "",
                            deckCategories = deck?.categories ?: emptyList(),
                            deckFlashcards = flashcards,
                            isLoading = false,
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}
