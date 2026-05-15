package com.example.cognilink.ui.feature.flashcard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cognilink.R
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.ui.components.flashcard.FlashcardOption

class IAGeneratorViewModel : ViewModel() {

    var flashcardTheme by mutableStateOf("")
        private set

    var quantity by mutableIntStateOf(1)
        private set

    var selectedDifficulty by mutableStateOf<DifficultyLevel?>(null)
        private set

    val typeOptions = listOf(
        FlashcardOption(1, "Pergunta e Resposta", "Ideal para fatos diretos", R.drawable.ic_basic_card),
        FlashcardOption(2, "Múltipla Escolha", "Ótimo para exames", R.drawable.ic_multiple_choice_card),
        FlashcardOption(3, "Omissão de Palavras", "Cloze deletion para memorização de contexto", R.drawable.ic_cloze_card),
        FlashcardOption(4, "Verdadeiro ou Falso", "Decisões rápidas e validação de conceitos", R.drawable.ic_true_or_false_card),
        FlashcardOption(5, "Chat de Feynman", "Explique conceitos complexos de forma simples", R.drawable.ic_chat_feynman),
        FlashcardOption(6, "Aleatório", "Misture todos os estilos para máximo desafio", R.drawable.ic_die)
    )

    var selectedType by mutableStateOf(typeOptions.last())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onThemeChange(newTheme: String) {
        flashcardTheme = newTheme
    }

    fun onQuantityChange(newQuantity: Int) {
        quantity = newQuantity
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel?) {
        selectedDifficulty = newDifficulty
    }

    fun onTypeChange(newType: FlashcardOption) {
        selectedType = newType
    }

    fun generateFlashcards() {
        // Logica para chamar a IA
        isLoading = true
        // Simulando chamada
        // viewModelScope.launch { ... }
    }
}
