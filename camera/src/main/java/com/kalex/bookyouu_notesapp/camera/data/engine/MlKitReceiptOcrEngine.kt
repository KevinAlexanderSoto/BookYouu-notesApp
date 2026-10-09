package com.kalex.bookyouu_notesapp.camera.data.engine

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.kalex.bookyouu_notesapp.camera.domain.engine.ReceiptOcrEngine
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Concrete implementation of [ReceiptOcrEngine] using Google ML Kit's bundled text recognizer.
 * Runs 100% on-device and completely offline.
 */
class MlKitReceiptOcrEngine(
    private val textRecognizer: TextRecognizer,
) : ReceiptOcrEngine {

    override suspend fun recognizeText(bitmap: Bitmap): Result<String> = runCatching {
        suspendCancellableCoroutine { continuation ->
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            textRecognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    if (continuation.isActive) {
                        continuation.resume(visionText.text)
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resumeWithException(exception)
                    }
                }
        }
    }
}
