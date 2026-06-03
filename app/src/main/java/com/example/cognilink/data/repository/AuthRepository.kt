package com.example.cognilink.data.repository

import com.example.cognilink.data.datebase.dao.UserDao
import com.example.cognilink.data.datebase.dao.UserStatsDao
import com.example.cognilink.data.mappers.toDomain
import com.example.cognilink.data.mappers.toEntity
import com.example.cognilink.data.model.User
import com.example.cognilink.data.model.UserStats
import kotlinx.coroutines.flow.first
import java.util.UUID

interface AuthRepository {
    suspend fun signIn(email: String, password: String): User?
    suspend fun signUp(name: String, email: String, password: String): User?
    suspend fun signOut()
}

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val userStatsDao: UserStatsDao
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): User? {
        // Busca o usuário pelo e-mail
        val userEntity = userDao.findUserByEmail(email) ?: return null
        
        // Em um sistema real com Firebase, a senha seria validada pelo Firebase.
        // Aqui buscamos as estatísticas para montar o objeto de domínio completo.
        val statsEntity = userStatsDao.getUserStatsByUserId(userEntity.id).first()
        val stats = statsEntity?.toDomain() ?: UserStats(userId = userEntity.id)
        
        return userEntity.toDomain(stats)
    }

    override suspend fun signUp(name: String, email: String, password: String): User? {
        // Verifica se o usuário já existe
        if (userDao.findUserByEmail(email) != null) return null

        // Cria um ID único (Simulando o UID do Firebase)
        val newUserId = UUID.randomUUID().toString()

        val newUser = User(
            id = newUserId,
            name = name,
            email = email,
            stats = UserStats(userId = newUserId)
        )

        // Salva no Room
        userDao.saveUser(newUser.toEntity())
        userStatsDao.insertUserStats(newUser.stats.toEntity())

        return newUser
    }

    override suspend fun signOut() {
        // Limparia sessão do Firebase aqui
    }
}
