package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.Deck
import com.example.cognilink.data.repository.DeckRepository
import com.example.cognilink.data.repository.FlashcardRepository
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.ui.states.DeckFormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckFormViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeckFormUiState())
    val uiState: StateFlow<DeckFormUiState> = _uiState.asStateFlow()

    fun initialize(deckId: String?, userId: String) {
        if (_uiState.value.deckId == deckId && _uiState.value.userId == userId) return
        
        val targetDeckId = deckId ?: _uiState.value.deckId

        _uiState.update { currentState ->
            currentState.copy(
                userId = userId,
                deckId = targetDeckId,
                isEditMode = deckId != null
            )
        }
        
        if (deckId != null) {
            loadDeckData()
        } else {
            // Se for um novo deck, ainda assim precisamos observar os flashcards 
            // que forem criados para este deckId gerado.
            loadFlashcards(targetDeckId)
        }
    }

    fun toggleRemoveMode() {
        _uiState.update { it.copy(isRemoveMode = !it.isRemoveMode) }
    }

    fun removeFlashcard(flashcardId: String) {
        viewModelScope.launch {
            try {
                flashcardRepository.deleteFlashcard(flashcardId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Erro ao remover flashcard") }
            }
        }
    }

    fun onDeckNameChange(newValue: String) {
        _uiState.update { it.copy(deckName = newValue, wasEdited = true, deckNameError = null) }
    }

    fun onDeckDescriptionChange(newValue: String) {
        _uiState.update { it.copy(deckDescription = newValue, wasEdited = true) }
    }

    fun onCategoryTextChange(newText: String) {
        _uiState.update { it.copy(categoryText = newText, wasEdited = true) }
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
                    categoryBeingEdited = null,
                    wasEdited = true
                )
            }
        }
    }

    fun removeCategory(category: String) {
        _uiState.update { it.copy(deckCategories = it.deckCategories - category, wasEdited = true) }
    }

    private fun validate(): Boolean {
        val name = _uiState.value.deckName
        if (name.isBlank()) {
            _uiState.update { it.copy(deckNameError = "O nome do baralho é obrigatório") }
            return false
        }
        return true
    }

    fun saveDeck(): Boolean {
        viewModelScope.launch {
            if (saveDeckSuspending()) {
                _uiState.update { it.copy(isSaved = true) }
            }
        }
        return uiState.value.isSaved
    }

    suspend fun saveDeckSuspending(): Boolean {
        if (!validate()) return false

        val currentState = _uiState.value
        val userId = currentState.userId ?: return false

        _uiState.update { it.copy(isLoading = true) }
        return try {
            val deckToSave = Deck(
                id = currentState.deckId,
                userId = userId,
                name = currentState.deckName,
                description = currentState.deckDescription,
                categories = currentState.deckCategories,
                difficulty = DifficultyLevel.EASY,
                mastery = 0f,
                totalCards = 0,
                cardsToReview = 0
            )
            
            deckRepository.saveDeck(deckToSave, userId)

            _uiState.update {
                it.copy(
                    isLoading = false, 
                    errorMessage = null,
                ) 
            }
            true
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            false
        }
    }

    fun discardDeck() {
        val currentState = _uiState.value
        // Se o deck foi criado "na surdina" apenas para suportar a FK dos flashcards
        // e o usuário desistir sem salvar o deck oficialmente, nós o removemos.
        if (!currentState.isEditMode) {
            val userId = currentState.userId ?: return
            viewModelScope.launch {
                try {
                    deckRepository.deleteDeck(currentState.deckId, userId)
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = "Erro ao descartar alterações") }
                }
            }
        }
    }

    fun clearEvents() {
        _uiState.update { it.copy(isSaved = false, errorMessage = null) }
    }

    fun toggleChangeDialog() {
        _uiState.update { it.copy(showChangeDialog = !it.showChangeDialog) }
    }

    fun toggleAddFlashcardDialog() {
        _uiState.update { it.copy(showAddFlashcardDialog = !it.showAddFlashcardDialog) }
    }

    fun loadDeckData() {
        val currentState = _uiState.value
        val deckId = currentState.deckId
        val userId = currentState.userId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                deckRepository.getDeckById(deckId, userId).collect { deck ->
                    _uiState.update {
                        it.copy(
                            deckName = deck?.name ?: "",
                            deckDescription = deck?.description ?: "",
                            deckCategories = deck?.categories ?: emptyList(),
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
        loadFlashcards(deckId)
    }

    private fun loadFlashcards(deckId: String) {
        viewModelScope.launch {
            try {
                flashcardRepository.getFlashcardsForDeck(deckId).collect { flashcards ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            deckFlashcards = flashcards,
                            isLoading = false,
                        )
                    }
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao carregar flashcards") }
            }
        }
    }
}
