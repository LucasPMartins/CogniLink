package com.example.cognilink.domain.usecase

import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.domain.model.DifficultyLevel

class CalculateDifficultyLevelUseCase {

    operator fun invoke(flashcards: List<Flashcard>): DifficultyLevel{
        if (flashcards.isEmpty()) return DifficultyLevel.EASY
        return DifficultyLevel.fromAverage(flashcards.map{it.difficulty.weight}.average().toFloat())
    }
}