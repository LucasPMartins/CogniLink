package com.example.cognilink.data.repository

import com.example.cognilink.data.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): User?
    suspend fun signUp(name: String, email: String, password: String): User?
    suspend fun signOut()
}

class AuthRepositoryImpl : AuthRepository {
    override suspend fun signIn(email: String, password: String): User? {
        // Simulação de login
        return if (email == "lucas@email.com" && password == "123") {
            User(id = "user-123", name = "Lucas", email = email)
        } else {
            null
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): User? {
        // Simulação de registro
        return User(id = "user-123", name = name, email = email)
    }

    override suspend fun signOut() {
        // Simulação de logout
    }
}
