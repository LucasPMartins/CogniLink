package com.example.cognilink.data.datebase.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.cognilink.data.model.Answer
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.domain.model.FlashcardType

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["deckId"])]
)
data class FlashcardEntity(
    @PrimaryKey
    val id: String,
    val deckId: String,
    val question: String,
    val cardType: FlashcardType,
    val difficulty: DifficultyLevel,
    val answerOptions: List<Answer>,
    val hints: List<String>
)
