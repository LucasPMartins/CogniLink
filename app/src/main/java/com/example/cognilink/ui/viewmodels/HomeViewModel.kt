package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.UserStats
import com.example.cognilink.data.repository.DeckRepository
import com.example.cognilink.data.repository.DeckRepositoryImpl
import com.example.cognilink.data.repository.UserRepository
import com.example.cognilink.data.repository.UserRepositoryImpl
import com.example.cognilink.ui.states.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val userRepository: UserRepository,
    private val deckRepository: DeckRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun initialize(userId: String) {
        if (_uiState.value.userId == userId) return
        _uiState.update { it.copy(userId = userId) }
        loadHomeData()
    }

    private fun loadHomeData() {
        val currentState = _uiState.value
        val userId = currentState.userId ?: return

        // Observar baralhos de forma reativa
        viewModelScope.launch {
            deckRepository.getDecks(userId).collect { decks ->
                _uiState.update { it.copy(decks = decks) }
            }
        }

        // Observar estatísticas de forma reativa
        viewModelScope.launch {
            userRepository.getUserStats(userId).collect { stats ->
                val userStats = stats ?: UserStats(userId = userId)
                _uiState.update {
                    it.copy(
                        totalStudyTime = userStats.totalStudyTime,
                        overallMastery = userStats.overallMastery,
                        cardsDone = userStats.totalFlashcardsDone,
                        retentionRate = userStats.retentionRate
                    )
                }
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val user = userRepository.getUserById(userId = userId)

                _uiState.update {
                    it.copy(
                        userName = user?.name ?: "Usuário",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao carregar dados da Home"
                    )
                }
            }
        }
    }

    fun formatTime(milliseconds: Long): String {
        if (milliseconds <= 0) return "0s"

        val totalDays = TimeUnit.MILLISECONDS.toDays(milliseconds)
        val years = totalDays / 365
        val days = totalDays % 365
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60

        return when {
            years > 0 -> "${years}y"
            days > 0 -> "${days}d"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "${seconds}s"
        }
    }

    fun onSearchValueChange(newValue: String) {
        _uiState.update { it.copy(searchInput = newValue) }
        filterDecks(newValue)
    }

    private fun filterDecks(query: String) {
        // Agora que os dados são reativos via Flow no loadHomeData, 
        // a filtragem pode ser feita localmente ou o Flow pode ser combinado.
        // Por simplicidade aqui, vamos apenas filtrar a lista atual se necessário,
        // mas o ideal seria usar um StateFlow para a query e combiná-lo com o Flow dos decks.
    }

    fun refreshDecks() {
        // Não é mais necessário com Flow reativo, mas mantemos para compatibilidade
    }
}
