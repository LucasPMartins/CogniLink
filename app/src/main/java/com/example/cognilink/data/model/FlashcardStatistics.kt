package com.example.cognilink.data.model

data class FlashcardStatistics(
    val idFlashcard: Long,
    val hits: Int,
    val misses: Int,
    val studyTime: Long,
    /**
     * LATÊNCIA DO CARD: Tempo médio que o usuário leva para evocar a resposta DESTE card.
     * Se o usuário acerta (hit), mas a latência é alta (ex: 7 segundos), o esforço cognitivo
     * ainda é alto. Se a latência for baixa (ex: 1.2 segundos), o conhecimento foi automatizado.
     */
    val averageLatencyMs: Long,

    /**
     * ESTABILIDADE DA MEMÓRIA (S): O intervalo atual calculado (em dias) em que a
     * probabilidade de recall deste card se mantém acima de 90%.
     */
    val memoryStabilityDays: Float,

    /**
     * FATOR DE FACILIDADE (Ease Factor - EF): Herdado do SM-2.
     * Mede o quão "fácil" ou "difícil" esse card é para o usuário com base no histórico.
     * Default começa em 2.5f. Diminui a cada erro e aumenta a cada acerto fácil.
     */
    val easeFactor: Float,

    /**
     * CONTEXTO ANCORA: O contexto onde este card específico performa melhor.
     * Útil para o CogniLink priorizar este card quando o usuário entrar neste cenário espacial/temporal.
     */
    val bestPerformingContext: String?,

    /**
     * CONTADOR DE FALHAS CONSECUTIVAS: Se atingir um limiar (ex: 4 ou 5), o card
     * é marcado como "Leech" (Sanguessuga).
     */
    val consecutiveMisses: Int,

    /**
     * TAXA DE RETENÇÃO INDIVIDUAL: Porcentagem de acertos em relação ao total de revisões.
     */
    val retentionRate: Float,

    /**
     * GRAU DE DOMÍNIO: Valor de 0.0 a 1.0 que representa o quão consolidado está o conhecimento.
     */
    val mastery: Float
)

val fakeFlashcardStatistics = listOf(
    FlashcardStatistics(
        idFlashcard = 1L,
        hits = 15,
        misses = 3,
        studyTime = 120000L,
        averageLatencyMs = 1500L,
        memoryStabilityDays = 4.5f,
        easeFactor = 2.6f,
        bestPerformingContext = "HOME_QUIET",
        consecutiveMisses = 0,
        retentionRate = 0.83f,
        mastery = 0.85f
    ),
    FlashcardStatistics(
        idFlashcard = 2L,
        hits = 5,
        misses = 8,
        studyTime = 300000L,
        averageLatencyMs = 7000L,
        memoryStabilityDays = 0.5f,
        easeFactor = 1.3f,
        bestPerformingContext = null,
        consecutiveMisses = 3,
        retentionRate = 0.38f,
        mastery = 0.20f
    )
)