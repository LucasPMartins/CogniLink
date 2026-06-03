package com.example.cognilink.data.repository

import com.example.cognilink.data.datebase.dao.DeckDao
import com.example.cognilink.data.mappers.toDomain
import com.example.cognilink.data.mappers.toEntity
import com.example.cognilink.data.model.Deck
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface DeckRepository {
    suspend fun getDecks(userId: String): List<Deck>
    suspend fun getDeckById(deckId: String, userId: String): Deck?
    suspend fun saveDeck(deck: Deck, userId: String)
    suspend fun deleteDeck(deckId: String, userId: String)
}

class DeckRepositoryImpl(private val deckDao: DeckDao) : DeckRepository {
    
    override suspend fun getDecks(userId: String): List<Deck> {
        // Busca os baralhos filtrados por usuário diretamente no Banco de Dados
        return deckDao.getDecksByUserId(userId).first().map { it.toDomain() }
    }

    override suspend fun getDeckById(deckId: String, userId: String): Deck? {
        val entity = deckDao.getDeckById(deckId)
        // Verificação de segurança: garante que o deck pertence ao usuário
        return if (entity?.userId == userId) entity.toDomain() else null
    }

    override suspend fun saveDeck(deck: Deck, userId: String) {
        // Garante que o userId está correto antes de salvar/atualizar
        deckDao.insertDeck(deck.toEntity().copy(userId = userId))
    }

    override suspend fun deleteDeck(deckId: String, userId: String) {
        val entity = deckDao.getDeckById(deckId)
        if (entity?.userId == userId) {
            deckDao.deleteDeckById(deckId)
        }
    }
}
