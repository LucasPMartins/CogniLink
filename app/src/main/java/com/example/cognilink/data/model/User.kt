package com.example.cognilink.data.model

/**
 * Representa o usuário principal do sistema e seus dados agregados.
 */
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val stats: UserStats,
)


val fakeUser = User(
    id = 1001L,
    name = "Alex Silva",
    email = "alex.silva@example.com",
    stats = fakeUserStats
)





