package com.example.cognilink.data.model

import com.example.cognilink.domain.model.CogniRank
import com.example.cognilink.domain.model.UserRankingResult
import java.util.UUID

data class UserStats(
    val userId: String = UUID.randomUUID().toString(),
    val totalFlashcardsMisses: Int = 0,
    val totalFlashcardsHits: Int = 0,
    val lastReview: Long = 0L,
    val totalStudyTime: Long = 0L,
    val totalFlashcardsDone: Int = 0,
    val totalFlashcardsReviewed: Int = 0,
    val overallMastery: Float = 0f,
    val retentionRate: Float = 0f,
    val cognitiveEfficiencyIndex: Float = 0f,
    val globalAverageLatencyMs: Long = 0L,
    val retentionByContext: Map<String, Float> = emptyMap(),
    val contextTriggerConversionRate: Float = 0f,
    val activeLeechesCount: Int = 0,
)

val fakeUserStats = UserStats(
    userId = "user-123",
    totalFlashcardsMisses = 11,
    totalFlashcardsHits = 20,
    lastReview = 0L,
    totalStudyTime = 420000L,
    totalFlashcardsDone = 100,
    totalFlashcardsReviewed = 31,
    overallMastery = 0.65f,
    retentionRate = 0.64f,
    cognitiveEfficiencyIndex = 1.2f,
    globalAverageLatencyMs = 4250L,
    retentionByContext = mapOf(
        "GEO_LIBRARY" to 0.85f,
        "TIME_22PM" to 0.40f,
        "HOME_QUIET" to 0.92f
    ),
    contextTriggerConversionRate = 0.75f,
    activeLeechesCount = 2
)
