package com.lucasdpm.cognilink.domain.repository

interface SimilarityService {
    suspend fun calculateSimilarity(text1: String, text2: String): Float
}
