package com.example.cognilink.viewModel

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
        else DifficultyLevel.Companion.fromAverage(deckFlashcards.map { it.difficulty.weight }
            .average().toFloat())
    }

    var deckMastery by mutableStateOf(null as Float?)
        private set

    val deckTotalCards by derivedStateOf { deckFlashcards.size }

    var deckCardsToReview by mutableStateOf(0)
        private set

    var showCategoryDialog by mutableStateOf(false)
        private set

    var categoryBeingEdited by mutableStateOf<String?>(null)
        private set

    var categoryText by mutableStateOf("")
        private set

    fun onCategoryTextChange(newText: String) {
        categoryText = newText
    }

    fun openCategoryDialog(category: String? = null) {
        categoryBeingEdited = category
        categoryText = category ?: ""
        showCategoryDialog = true
    }

    fun closeCategoryDialog() {
        showCategoryDialog = false
        categoryText = ""
        categoryBeingEdited = null
    }

    fun handleCategoryConfirmation() {
        if (categoryText.isNotBlank()) {
            val oldName = categoryBeingEdited
            if (oldName == null) {
                addCategory(categoryText)
            } else {
                updateCategory(oldName, categoryText)
            }
            closeCategoryDialog()
        }
    }
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