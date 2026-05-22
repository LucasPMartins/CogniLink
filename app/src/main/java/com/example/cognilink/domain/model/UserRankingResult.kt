package com.example.cognilink.domain.model

data class UserRankingResult(
    val currentRank: CogniRank,
    val finalScore: Float, // De 0.0 a 100.0
    val dynamicInsight: String,
    val efficiencyInsight: String,
    val retentionInsight: String
)