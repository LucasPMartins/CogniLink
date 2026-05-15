package com.example.cognilink.ui.feature

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashCard

class DeckViewModel : ViewModel() {
    var isEditMode by mutableStateOf(false)
        private set

    var deckName by mutableStateOf("")
        private set

    val deckCategories = mutableStateListOf<String>()

    var deckDescription by mutableStateOf("")
        private set

    val deckFlashcards = mutableStateListOf<FlashCard>()

    val deckDifficulty by derivedStateOf {
        if (deckFlashcards.isEmpty()) null
        else DifficultyLevel.fromAverage(deckFlashcards.map { it.difficulty.weight }.average().toFloat())
    }

    var deckMastery by mutableStateOf(null as Float?)
        private set

    val deckTotalCards by derivedStateOf { deckFlashcards.size }

    var deckCardsToReview by mutableStateOf(0)
        private set

    fun toggleEditMode() {
        isEditMode = !isEditMode
    }

    fun onDeckNameChange(newValue: String) {
        deckName = newValue
    }

    fun addCategory(category: String) {
        deckCategories.add(category)
    }

    fun removeCategory(category: String) {
        deckCategories.remove(category)
    }

    fun updateCategory(oldCategory: String, newCategory: String) {
        val index = deckCategories.indexOf(oldCategory)
        if (index != -1 && newCategory.isNotBlank()) {
            deckCategories[index] = newCategory
        }
    }

    fun onDeckDescriptionChange(newValue: String) {
        deckDescription = newValue
    }

    fun onDeckCardsToReviewChange(newValue: Int){
        deckCardsToReview = newValue
    }

    fun onDeckMasteryChange(newValue: Float){
        deckMastery = newValue
    }
}