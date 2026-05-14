package com.example.cognilink.data

enum class DifficultyLevel(val weight: Int) {
    EAZY(1), MEDIUM(2), HARD(3);

    fun toDisplayName() = when(this) {
        EAZY -> "FÁCIL"
        MEDIUM -> "MÉDIO"
        HARD -> "DIFÍCIL"
    }

    companion object {
        fun fromWeight(weight: Int): DifficultyLevel {
            return entries.find { it.weight == weight } ?: MEDIUM
        }
        
        fun fromAverage(average: Float): DifficultyLevel {
            return when {
                average <= 1.5f -> EAZY
                average <= 2.5f -> MEDIUM
                else -> HARD
            }
        }
    }
}