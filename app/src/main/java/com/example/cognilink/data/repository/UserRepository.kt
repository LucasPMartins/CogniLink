package com.example.cognilink.data.repository

import com.example.cognilink.data.datebase.dao.UserDao
import com.example.cognilink.data.datebase.dao.UserStatsDao
import com.example.cognilink.data.mappers.toDomain
import com.example.cognilink.data.mappers.toEntity
import com.example.cognilink.data.model.User
import com.example.cognilink.data.model.UserStats
import kotlinx.coroutines.flow.first

interface UserRepository {
    suspend fun getUserById(userId: String): User?
    suspend fun getUserStats(userId: String): UserStats?
    suspend fun updateUser(user: User)
}

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val userStatsDao: UserStatsDao
) : UserRepository {
    override suspend fun getUserById(userId: String): User? {
        val userEntity = userDao.findUserById(userId) ?: return null
        val statsEntity = userStatsDao.getUserStatsByUserId(userId).first()
        val stats = statsEntity?.toDomain() ?: UserStats(userId = userId)
        return userEntity.toDomain(stats)
    }

    override suspend fun getUserStats(userId: String): UserStats? {
        return userStatsDao.getUserStatsByUserId(userId).first()?.toDomain()
    }

    override suspend fun updateUser(user: User) {
        userDao.saveUser(user.toEntity())
        userStatsDao.insertUserStats(user.stats.toEntity())
    }
}
