package com.example.cognilink.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface TermsRepository {
    suspend fun searchTermsFromApi(): TermsResponse
}

data class TermsResponse(val content: String)

class TermsRepositoryImpl : TermsRepository {
    override suspend fun searchTermsFromApi(): TermsResponse {
        // TODO: Implementação real chamaria um serviço Retrofit ou Ktor
        return TermsResponse(content = "Termos de uso carregados da nuvem.")
    }
}

interface LocalAssetLoader {
    fun loadJsonFromAssets(fileName: String): String
}

class LocalAssetLoaderImpl(private val context: Context) : LocalAssetLoader {
    override fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}

class TermsViewModel(
    private val repository: TermsRepository,
    private val localAssetLoader: LocalAssetLoader
) : ViewModel() {

    var termsText by mutableStateOf("Carregando...")
        private set

    init {
        loadTerms()
    }

    fun refreshTerms() {
        loadTerms()
    }

    private fun loadTerms() {
        viewModelScope.launch {
            termsText = "Carregando..."
            try {
                // Tenta buscar da API na nuvem
                val responseAPI = withContext(Dispatchers.IO) {
                    repository.searchTermsFromApi()
                }
                termsText = responseAPI.content
            } catch (e: Exception) {
                // Se der erro (sem internet, ex), carrega o arquivo local
                try {
                    val localJson = withContext(Dispatchers.IO) {
                        localAssetLoader.loadJsonFromAssets("terms_fallback.json")
                    }
                    termsText = localJson
                } catch (fallbackException: Exception) {
                    termsText = "Erro ao carregar termos de uso."
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            repository: TermsRepository,
            localAssetLoader: LocalAssetLoader
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TermsViewModel::class.java)) {
                    return TermsViewModel(repository, localAssetLoader) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        fun provideFactory(context: Context): ViewModelProvider.Factory {
            val repository = TermsRepositoryImpl()
            val localAssetLoader = LocalAssetLoaderImpl(context.applicationContext)
            return provideFactory(repository, localAssetLoader)
        }
    }
}