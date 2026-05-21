package com.example.cognilink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.UserStats
import com.example.cognilink.data.repository.ProfileRepository
import com.example.cognilink.domain.model.UserRankingResult
import com.example.cognilink.domain.usecase.CalculateUserRankingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(
        val userName: String,
        val stats: UserStats,
        val ranking: UserRankingResult
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

class ProfileViewModel(
    private val calculateUserRankingUseCase: CalculateUserRankingUseCase = CalculateUserRankingUseCase(),
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfileData()
    }

    fun loadUserProfileData() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val userStats = repository.getUserStats()
                val rankingResult = calculateUserRankingUseCase(userStats)

                _uiState.value = ProfileUiState.Success(
                    userName = "Usuário",
                    stats = userStats,
                    ranking = rankingResult
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun formatTime(milliseconds: Long): String {
        if (milliseconds <= 0) return "0s"

        val days = TimeUnit.MILLISECONDS.toDays(milliseconds)
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60

        val parts = mutableListOf<String>()
        if (days > 0) parts.add("${days}d")
        if (hours > 0) parts.add("${hours}h")
        if (minutes > 0) parts.add("${minutes}m")
        if (seconds > 0) parts.add("${seconds}s")

        return parts.joinToString(" ")
    }

    fun formatLastReview(lastReviewTimestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - lastReviewTimestamp
        if (diff < 0) return "Agora mesmo"

        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60

        return when {
            days > 0 -> "$days dias atrás Última Revisão"
            hours > 0 -> "${hours}h atrás Última Revisão"
            minutes > 0 -> "${minutes}min atrás Última Revisão"
            else -> "Agora mesmo"
        }
    }

    fun formatLatency(latencyMs: Long): String {
        return String.format(Locale.getDefault(), "%.1f ms", latencyMs.toDouble())
    }
}
