package com.example.cognilink.data.datebase.entities

import androidx.room.Embedded

data class DeckWithStatsEntity(
    @Embedded val deck: DeckEntity,
    val totalCards: Int,
    val cardsToReview: Int,
    val averageDifficulty: Float,
    val averageMastery: Float
)
