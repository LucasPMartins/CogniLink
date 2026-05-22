package com.example.cognilink.ui.states

import com.example.cognilink.data.model.UserStats
import com.example.cognilink.domain.model.UserRankingResult

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(
        val userName: String,
        val stats: UserStats,
        val ranking: UserRankingResult
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}