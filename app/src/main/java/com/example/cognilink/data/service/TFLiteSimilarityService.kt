package com.example.cognilink.data.service

import android.content.Context
import android.util.Log
import com.example.cognilink.domain.repository.SimilarityService
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.text.textembedder.TextEmbedder
import com.google.mediapipe.tasks.text.textembedder.TextEmbedder.TextEmbedderOptions
import kotlin.math.sqrt

class TFLiteSimilarityService(private val context: Context) : SimilarityService {

    private val modelPath = "all-MiniLM-L6-v2-quant.tflite"

    private val textEmbedder by lazy {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath(modelPath)
                .build()
            val options = TextEmbedderOptions.builder()
                .setBaseOptions(baseOptions)
                .build()
            TextEmbedder.createFromOptions(context, options)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun calculateSimilarity(text1: String, text2: String): Float {
        if (text1.trim().equals(text2.trim(), ignoreCase = true)) return 1.0f

        val embedder = textEmbedder ?: run {
            Log.e("TFLiteSimilarity", "TextEmbedder não pôde ser inicializado")
            return 0.5f
        }

        return try {
            val result1 = embedder.embed(text1)
            val result2 = embedder.embed(text2)

            val v1 = result1.embeddingResult().embeddings()[0].floatEmbedding()
            val v2 = result2.embeddingResult().embeddings()[0].floatEmbedding()

            cosineSimilarity(v1, v2)
        } catch (e: Exception) {
            e.printStackTrace()
            0.5f
        }
    }

    private fun cosineSimilarity(v1: FloatArray, v2: FloatArray): Float {
        var dotProduct = 0.0f
        var normA = 0.0f
        var normB = 0.0f
        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }
        val denominator = sqrt(normA.toDouble()) * sqrt(normB.toDouble())
        return if (denominator > 0) (dotProduct / denominator).toFloat() else 0.0f
    }
}
