package com.example.cognilink.data.repository

import com.example.cognilink.data.model.Deck

interface DeckRepository {
    suspend fun getDecks(userId: String): List<Deck>
    suspend fun getDeckById(deckId: String, userId: String): Deck?
    suspend fun saveDeck(deck: Deck, userId: String)
    suspend fun deleteDeck(deckId: String, userId: String)
}

class DeckRepositoryImpl : DeckRepository {
    override suspend fun getDecks(userId: String): List<Deck> {
        return emptyList()
    }

    override suspend fun getDeckById(deckId: String, userId: String): Deck? {
        return null
    }

    override suspend fun saveDeck(deck: Deck, userId: String) {
        println("Deck ${deck.name} salvo com sucesso para o usuário: $userId")
    }

    override suspend fun deleteDeck(deckId: String, userId: String) {
        println("Deck com ID $deckId deletado para o usuário: $userId")
    }
}
