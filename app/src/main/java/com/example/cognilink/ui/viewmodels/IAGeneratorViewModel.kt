package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.ui.states.IAGeneratorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class IAGeneratorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(IAGeneratorUiState())
    val uiState: StateFlow<IAGeneratorUiState> = _uiState.asStateFlow()

    fun initialize(deckId: String) {
        if (_uiState.value.deckId == deckId) return
        _uiState.update { it.copy(deckId = deckId) }
    }

    fun onThemeChange(newTheme: String) {
        _uiState.update { it.copy(flashcardTheme = newTheme) }
    }

    fun onQuantityChange(newQuantity: Int) {
        _uiState.update { it.copy(quantity = newQuantity) }
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel?) {
        _uiState.update { it.copy(selectedDifficulty = newDifficulty) }
    }

    fun onTypeChange(newType: FlashcardType?) {
        _uiState.update { it.copy(selectedType = newType) }
    }

    fun onUploadFile(){
        //TODO
    }

    fun generateFlashcards() {
        val deckId = _uiState.value.deckId ?: return
        _uiState.update { it.copy(isLoading = true) }
        // TODO: Lógica para chamar a IA
        // viewModelScope.launch { ... }
    }
}
