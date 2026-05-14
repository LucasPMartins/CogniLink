package com.example.cognilink.ui.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cognilink.data.DifficultyLevel

class DeckViewModel : ViewModel() {
    var isEditMode by mutableStateOf(false)
        private set

    var deckName by mutableStateOf("")
        private set

    var deckCategory by mutableStateOf("")
        private set

    var deckDescription by mutableStateOf("")
        private set

    var deckDifficulty by mutableStateOf(null as DifficultyLevel?)
        private set

    var deckMastery by mutableStateOf(null as Float?)
        private set

    fun toggleEditMode() {
        isEditMode = !isEditMode
    }

    fun onDeckNameChange(newValue: String) {
        deckName = newValue
    }

    fun onDeckCategoryChange(newValue: String) {
        deckCategory = newValue
    }

    fun onDeckDescriptionChange(newValue: String) {
        deckDescription = newValue
    }

}