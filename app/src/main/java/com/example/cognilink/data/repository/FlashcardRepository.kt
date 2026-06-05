package com.example.cognilink.data.repository

import com.example.cognilink.data.datebase.dao.DeckDao
import com.example.cognilink.data.datebase.dao.FlashcardDao
import com.example.cognilink.data.datebase.dao.FlashcardStatsDao
import com.example.cognilink.data.mappers.toDomain
import com.example.cognilink.data.mappers.toEntity
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.model.FlashcardStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface FlashcardRepository {
    fun getFlashcardsForDeck(deckId: String): Flow<List<Flashcard>>
    suspend fun saveFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcardId: String)
    suspend fun getFlashcardById(flashcardId: String): Flashcard?
    suspend fun saveAllFlashcards(flashcards: List<Flashcard>)
    fun getFlashcardStatistics(flashcardId: String): Flow<FlashcardStats?>
    suspend fun getLeeches(userId: String): List<Flashcard>?
    suspend fun getReviewPending(userId: String): List<Flashcard>?
    suspend fun getDeckName(deckId: String): String?
    suspend fun getReviewCountForDeck(deckId: String, todayTimestamp: Long): Flow<Int>
    suspend fun updateFlashcardStatistics(statistics: FlashcardStats)
    suspend fun getFlashcardsToReview(deckId: String, currentTimestamp: Long): List<Flashcard>
    suspend fun resetStatistics(flashcardId: String)
}

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao,
    private val flashcardStatsDao: FlashcardStatsDao,
    private val deckDao: DeckDao
) : FlashcardRepository {
    override fun getFlashcardsForDeck(deckId: String): Flow<List<Flashcard>> {
        return flashcardDao.getFlashcardForDeckById(deckId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveFlashcard(flashcard: Flashcard) {
        flashcardDao.insertFlashcard(flashcard.toEntity())
    }

    override suspend fun deleteFlashcard(flashcardId: String) {
        flashcardDao.deleteFlashcardById(flashcardId)
    }

    override suspend fun getFlashcardById(flashcardId: String): Flashcard? {
        return flashcardDao.getFlashcardById(flashcardId)?.toDomain()
    }

    override suspend fun saveAllFlashcards(flashcards: List<Flashcard>) {
        flashcardDao.saveAllFlashcards(flashcards.map { it.toEntity() })
    }

    override fun getFlashcardStatistics(flashcardId: String): Flow<FlashcardStats?> {
        return flashcardStatsDao.getFlashcardStatsById(flashcardId).map { it?.toDomain() }
    }

    override suspend fun getLeeches(userId: String): List<Flashcard>? {
        // Implementar lógica de busca de leeches baseada nas estatísticas
        // Por agora retornamos vazio ou implementamos se houver query no DAO
        return null
    }

    override suspend fun getReviewPending(userId: String): List<Flashcard>? {
        // Implementar lógica de cards pendentes
        return null
    }

    override suspend fun getDeckName(deckId: String): String? {
        return deckDao.getDeckById(deckId).first()?.name
    }

    override suspend fun getReviewCountForDeck(deckId: String, todayTimestamp: Long): Flow<Int> {
        // Exemplo de como calcular a contagem de revisão via estatísticas
        // Isso requer uma query mais complexa ou processamento do Flow
        return flashcardDao.getFlashcardForDeckById(deckId).map { cards ->
            cards.size // Simulação: todos os cards precisam de revisão
        }
    }

    override suspend fun updateFlashcardStatistics(statistics: FlashcardStats) {
        flashcardStatsDao.insertFlashcardStats(statistics.toEntity())
    }

    override suspend fun getFlashcardsToReview(deckId: String, currentTimestamp: Long): List<Flashcard> {
        // Filtrar cards que possuem nextReview <= currentTimestamp
        return emptyList()
    }

    override suspend fun resetStatistics(flashcardId: String) {
        flashcardStatsDao.deleteFlashcardStatsById(flashcardId)
    }
}
