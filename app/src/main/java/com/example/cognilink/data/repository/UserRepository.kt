package com.example.cognilink.data.repository

import com.example.cognilink.data.model.User
import com.example.cognilink.data.model.UserStats

interface UserRepository {
    suspend fun getUserById(userId: String): User?
    suspend fun getUserStats(userId: String): UserStats?
    suspend fun updateUser(user: User)
}

class UserRepositoryImpl : UserRepository {
    override suspend fun getUserById(userId: String): User? {
        return User(id = userId, name = "John Doe", email = "john.doe@example.com")
    }

    override suspend fun getUserStats(userId: String): UserStats? {
        return null
    }

    override suspend fun updateUser(user: User) {
    }
}
