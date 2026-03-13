package com.nutriscan.ml

import android.graphics.Rect
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.tasks.await

class BarcodeScanner {
    private val scanner = BarcodeScanning.getClient()
    
    suspend fun scanBarcode(inputImage: InputImage): String? {
        return try {
            val barcodes = scanner.process(inputImage).await()
            barcodes.firstOrNull()?.rawValue
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun release() {
        scanner.close()
    }
}
