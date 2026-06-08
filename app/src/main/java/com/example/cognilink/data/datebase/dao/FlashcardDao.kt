package com.example.cognilink.data.datebase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.cognilink.data.datebase.entities.FlashcardEntity
import com.example.cognilink.data.datebase.entities.FlashcardWithStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): Flow<List<FlashcardEntity>>

    @Transaction
    @Query("SELECT * FROM flashcards WHERE id = :id")
    suspend fun getFlashcardById(id: String): FlashcardWithStatsEntity?

    @Transaction
    @Query("SELECT * FROM flashcards WHERE deckId = :id")
    fun getFlashcardForDeckById(id: String): Flow<List<FlashcardWithStatsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllFlashcards(flashcards: List<FlashcardEntity>)

    @Query("DELETE FROM flashcards")
    suspend fun deleteAllFlashcards()

    @Query("DELETE FROM flashcards WHERE id = :id")
    suspend fun deleteFlashcardById(id: String)
}