package com.example.cognilink.data.model

import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.domain.model.DifficultyLevel
import java.util.UUID

data class Flashcard(
    val id: String = UUID.randomUUID().toString(),
    val deckId: String, // Referência ao deck pai
    val question: String,
    val cardType: FlashcardType,
    val difficulty: DifficultyLevel,
    var answerOptions: List<Answer>,
    val hints: List<String>
)

data class Answer(
    val answer: String,
    val isCorrect: Boolean
)

//Fake Objects
val flashcard1 = Flashcard(
    deckId = "deck-123",
    question = "Qual é a capital da França?",
    cardType = FlashcardType.MULTIPLE_CHOICE,
    difficulty = DifficultyLevel.EASY,
    answerOptions = listOf(
        Answer("Paris", true),
        Answer("Londres", false),
        Answer("Roma", false)
    ),
    hints = listOf("Dica 1", "Dica 2")
)
