package com.example.cognilink.data.repository

import androidx.room.withTransaction
import com.example.cognilink.data.datebase.CogniLinkDatabase
import com.example.cognilink.data.datebase.dao.UserDao
import com.example.cognilink.data.datebase.dao.UserStatsDao
import com.example.cognilink.data.mappers.toDomain
import com.example.cognilink.data.mappers.toEntity
import com.example.cognilink.data.model.User
import com.example.cognilink.data.model.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserRepository {
    suspend fun getUserById(userId: String): User?
    fun getUserStats(userId: String): Flow<UserStats?>
    suspend fun updateUser(user: User)
}

class UserRepositoryImpl(
    private val db: CogniLinkDatabase,
    private val userDao: UserDao,
    private val userStatsDao: UserStatsDao
) : UserRepository {
    override suspend fun getUserById(userId: String): User? {
        val userEntity = userDao.findUserById(userId) ?: return null
        return userEntity.toDomain(UserStats(userId = userId))
    }

    override fun getUserStats(userId: String): Flow<UserStats?> {
        return userStatsDao.getUserStatsByUserId(userId).map { it?.toDomain() }
    }

    override suspend fun updateUser(user: User) {
        db.withTransaction {
            userDao.saveUser(user.toEntity())
            userStatsDao.insertUserStats(user.stats.toEntity())
        }
    }
}
