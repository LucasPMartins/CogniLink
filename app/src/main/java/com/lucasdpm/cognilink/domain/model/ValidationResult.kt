package com.lucasdpm.cognilink.domain.model

sealed class ValidationResult {
    object Correct : ValidationResult()
    data class Feedback(val message: String) : ValidationResult()
    object Fallback : ValidationResult()
}
