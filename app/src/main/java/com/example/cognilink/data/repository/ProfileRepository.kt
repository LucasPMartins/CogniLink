package com.example.cognilink.data.repository

import com.example.cognilink.data.model.UserStats
import com.example.cognilink.data.model.fakeUser

class ProfileRepository {
    suspend fun getUserStats(): UserStats {
        // Simulação de busca de dados
        return fakeUser.stats
    }
}
