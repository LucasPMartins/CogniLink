package com.lucasdpm.cognilink.domain.usecase

import com.lucasdpm.cognilink.domain.model.ValidationResult
import com.lucasdpm.cognilink.domain.repository.FeedbackService
import com.lucasdpm.cognilink.domain.repository.NetworkMonitor
import com.lucasdpm.cognilink.domain.repository.SimilarityService

class ValidateBasicAnswerUseCase(
    private val similarityService: SimilarityService,
    private val feedbackService: FeedbackService,
    private val networkMonitor: NetworkMonitor
) {
    suspend operator fun invoke(userAnswer: String, correctAnswer: String): ValidationResult {
        // 1. Validação Local (TFLite)
        val score = similarityService.calculateSimilarity(userAnswer, correctAnswer)
        
        if (score >= 0.85f) {
            return ValidationResult.Correct
        }

        // 2. Validação Remota (LLM) se score for baixo
        return if (networkMonitor.isOnline()) {
            try {
                val feedback = feedbackService.getLlmFeedback(userAnswer, correctAnswer)
                ValidationResult.Feedback(feedback)
            } catch (e: Exception) {
                ValidationResult.Fallback
            }
        } else {
            ValidationResult.Fallback
        }
    }
}
