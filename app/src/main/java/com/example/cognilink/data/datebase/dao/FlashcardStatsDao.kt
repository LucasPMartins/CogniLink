package com.example.cognilink.data.datebase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cognilink.data.model.FlashcardStats
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardStatsDao {
    @Query("SELECT * FROM flashcards_stats WHERE flashcardId = :flashcardId")
    fun getStatsForFlashcard(flashcardId: Long): Flow<FlashcardStats?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: FlashcardStats)

    @Update
    suspend fun updateStats(stats: FlashcardStats)

    @Delete
    suspend fun deleteStats(stats: FlashcardStats)
}
