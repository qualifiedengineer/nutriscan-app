package com.nutriscan.ml

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

class TextRecognizer {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    suspend fun recognizeText(inputImage: InputImage): String {
        return try {
            val result = recognizer.process(inputImage).await()
            result.text
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    
    fun release() {
        recognizer.close()
    }
}
