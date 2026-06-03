package com.example.cognilink.data.model

import com.example.cognilink.domain.model.DifficultyLevel
import java.util.UUID

data class Deck(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val name:String,
    val categories: List<String>,
    val description: String,
    val difficulty: DifficultyLevel,
    val mastery: Float,
    val totalCards: Int,
    val cardsToReview: Int
)
