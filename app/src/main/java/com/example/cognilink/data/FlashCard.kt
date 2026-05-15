package com.example.cognilink.data

import java.util.Date

data class FlashCard(
    val question: String = "Questão exemplo",
    val cardType: FlashcardType = FlashcardType.BASIC,
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val nextReview: Date = Date(),
    var answers: List<Answer> = listOf()
)

data class Answer(
    val answer: String,
    val isCorrect: Boolean
)