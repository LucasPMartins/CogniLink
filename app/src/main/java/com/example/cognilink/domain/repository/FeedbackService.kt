package com.example.cognilink.domain.repository

interface FeedbackService {
    suspend fun getLlmFeedback(userAnswer: String, correctAnswer: String): String
}
