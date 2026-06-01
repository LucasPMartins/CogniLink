package com.example.cognilink.data.repository

import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.model.FlashcardStatistics
import com.example.cognilink.data.model.deck1
import com.example.cognilink.data.model.deck2
import com.example.cognilink.data.model.deck3
import com.example.cognilink.data.model.fakeFlashcardStatistics
import com.example.cognilink.data.model.flashcard1
import com.example.cognilink.data.model.flashcard2
import com.example.cognilink.data.model.flashcard3
import com.example.cognilink.data.model.flashcard4

interface FlashcardRepository {
    suspend fun getFlashcardsForDeck(deckId: Long): List<Flashcard>
    suspend fun saveFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcardId: String)

    suspend fun getFlashcardById(flashcardId: Long): Flashcard?

    suspend fun saveAllFlashcards(flashcards: List<Flashcard>)

    suspend fun getFlashcardStatistics(flashcardId: Long): FlashcardStatistics?

    suspend fun getLeeches(userId: Long): List<Flashcard>?

    suspend fun getReviewPending(userId: Long): List<Flashcard>?

    suspend fun getDeckName(deckId: Long): String?
}

class FlashcardRepositoryImpl : FlashcardRepository {
    override suspend fun getFlashcardsForDeck(deckId: Long): List<Flashcard> {
        // Filtra os flashcards pelo deckId (simulação)
        return listOf(flashcard1, flashcard2, flashcard3, flashcard4).filter { it.deckId == deckId }
    }

    override suspend fun saveFlashcard(flashcard: Flashcard) {
        // Simulação de salvar flashcard
    }

    override suspend fun deleteFlashcard(flashcardId: String) {
        // Simulação de deletar flashcard
    }

    override suspend fun getFlashcardById(flashcardId: Long): Flashcard? {
        return listOf(flashcard1, flashcard2, flashcard3, flashcard4).find { it.id == flashcardId }
    }

    override suspend fun saveAllFlashcards(flashcards: List<Flashcard>) {
        // Simulação de salvar todos os flashcards
    }

    override suspend fun getFlashcardStatistics(flashcardId: Long): FlashcardStatistics? {
        return fakeFlashcardStatistics.find { it.idFlashcard == flashcardId }
    }

    override suspend fun getLeeches(userId: Long): List<Flashcard>? {
        return null
    }

    override suspend fun getReviewPending(userId: Long): List<Flashcard>? {
        return null
    }

    override suspend fun getDeckName(deckId: Long): String? {
        return listOf(deck1, deck2, deck3).find { it.id == deckId }?.name
    }
}
