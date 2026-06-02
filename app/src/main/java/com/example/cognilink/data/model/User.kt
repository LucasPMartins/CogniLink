package com.example.cognilink.data.model

import java.util.UUID

/**
 * Representa o usuário principal do sistema e seus dados agregados.
 */
data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val email: String = "",
)

val fakeUser = User(
    id = "user-123",
    name = "Alex Silva",
    email = "alex.silva@example.com"
)
