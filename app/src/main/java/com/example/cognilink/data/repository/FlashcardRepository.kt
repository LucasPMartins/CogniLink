package com.example.cognilink.data.repository

import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.model.FlashcardStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface FlashcardRepository {
    suspend fun getFlashcardsForDeck(deckId: String): List<Flashcard>
    suspend fun saveFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcardId: String)
    suspend fun getFlashcardById(flashcardId: String): Flashcard?
    suspend fun saveAllFlashcards(flashcards: List<Flashcard>)
    suspend fun getFlashcardStatistics(flashcardId: String): FlashcardStats?
    suspend fun getLeeches(userId: String): List<Flashcard>?
    suspend fun getReviewPending(userId: String): List<Flashcard>?
    suspend fun getDeckName(deckId: String): String?
    suspend fun getReviewCountForDeck(deckId: String, todayTimestamp: Long): Flow<Int>
    suspend fun updateFlashcardStatistics(statistics: FlashcardStats)
    suspend fun getFlashcardsToReview(deckId: String, currentTimestamp: Long): List<Flashcard>
    suspend fun resetStatistics(flashcardId: String)
}

class FlashcardRepositoryImpl : FlashcardRepository {
    override suspend fun getFlashcardsForDeck(deckId: String): List<Flashcard> = emptyList()
    override suspend fun saveFlashcard(flashcard: Flashcard) {}
    override suspend fun deleteFlashcard(flashcardId: String) {}
    override suspend fun getFlashcardById(flashcardId: String): Flashcard? = null
    override suspend fun saveAllFlashcards(flashcards: List<Flashcard>) {}
    override suspend fun getFlashcardStatistics(flashcardId: String): FlashcardStats? = null
    override suspend fun getLeeches(userId: String): List<Flashcard>? = null
    override suspend fun getReviewPending(userId: String): List<Flashcard>? = null
    override suspend fun getDeckName(deckId: String): String? = null
    override suspend fun getReviewCountForDeck(deckId: String, todayTimestamp: Long): Flow<Int> = flowOf(3)
    override suspend fun updateFlashcardStatistics(statistics: FlashcardStats) {}
    override suspend fun getFlashcardsToReview(deckId: String, currentTimestamp: Long): List<Flashcard> = emptyList()
    override suspend fun resetStatistics(flashcardId: String) {}
}
