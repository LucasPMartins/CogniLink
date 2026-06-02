package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.repository.DeckRepository
import com.example.cognilink.data.repository.DeckRepositoryImpl
import com.example.cognilink.data.repository.FlashcardRepository
import com.example.cognilink.data.repository.FlashcardRepositoryImpl
import com.example.cognilink.ui.states.DeckUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckViewModel(
    private val deckRepository: DeckRepository = DeckRepositoryImpl(),
    private val flashcardRepository: FlashcardRepository = FlashcardRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeckUiState())
    val uiState: StateFlow<DeckUiState> = _uiState.asStateFlow()

    fun initialize(deckId: String, userId: String) {
        if (_uiState.value.deckId == deckId && _uiState.value.userId == userId) return
        _uiState.update { it.copy(deckId = deckId, userId = userId) }
        loadDeckDetails(deckId, userId)
    }

    private fun loadDeckDetails(deckId: String, userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val deck = deckRepository.getDeckById(deckId, userId)

                if (deck != null) {
                    _uiState.update { it.copy(currentDeck = deck) }
                    loadFlashcards(deck.id)
                } else {
                    _uiState.update { it.copy(errorMessage = "Baralho não encontrado") }
                }
                _uiState.update { it.copy(isLoading = false) }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erro ao carregar baralho"
                    )
                }
            }
        }
    }

    private fun loadFlashcards(deckId: String) {
        viewModelScope.launch {
            try {
                val flashcards = flashcardRepository.getFlashcardsForDeck(deckId)
                _uiState.update { it.copy(flashcards = flashcards.take(3)) }
            } catch (_: Exception) {
                _uiState.update { it.copy(errorMessage = "Erro ao carregar flashcards") }
            }
        }
    }

    fun loadAllFlashcards() {
        val deckId = _uiState.value.deckId ?: return
        viewModelScope.launch {
            try {
                val flashcards = flashcardRepository.getFlashcardsForDeck(deckId)
                _uiState.update { it.copy(flashcards = flashcards) }
            } catch (_: Exception) {
                _uiState.update { it.copy(errorMessage = "Erro ao carregar flashcards") }
            }
        }
    }

    fun deleteDeck() {
        val deckId = _uiState.value.deckId ?: return
        val userId = _uiState.value.userId ?: return
        viewModelScope.launch {
            deckRepository.deleteDeck(deckId, userId)
        }
    }

    fun toggleMenu() {
        _uiState.update { it.copy(isMenuExpanded = !it.isMenuExpanded) }
    }

    fun toggleAddFlashcardDialog() {
        _uiState.update { it.copy(isAddFlashcardDialogOpen = !it.isAddFlashcardDialogOpen) }
    }

    fun toggleDeleteDeckDialog() {
        _uiState.update { it.copy(isDeleteDeckDialogOpen = !it.isDeleteDeckDialogOpen) }
    }
}
