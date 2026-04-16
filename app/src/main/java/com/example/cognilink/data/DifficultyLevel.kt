package com.example.cognilink.data

enum class DifficultyLevel {
    EAZY, MEDIUM, HARD;

    fun toDisplayName() = when(this) {
        EAZY -> "FÁCIL"
        MEDIUM -> "MÉDIO"
        HARD -> "DIFÍCIL"
    }
}