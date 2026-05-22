package com.example.cognilink.domain.model

enum class CogniRank(val displayName: String, val minScore: Float) {
    NEURONIO_INICIANTE("Neurônio Iniciante", 0.0f),
    CONEXAO_SINAPTICA("Conexão Sináptica", 25.0f),
    MEMORIA_DE_SILICIO("Memória de Silício", 50.0f),
    MESTRE_DO_RECALL("Mestre do Recall", 75.0f),
    ARQUITETO_COGNITIVO("Arquiteto Cognitivo", 90.0f);

    companion object {
        fun fromScore(score: Float): CogniRank {
            return entries.lastOrNull { score >= it.minScore } ?: NEURONIO_INICIANTE
        }
    }
}

