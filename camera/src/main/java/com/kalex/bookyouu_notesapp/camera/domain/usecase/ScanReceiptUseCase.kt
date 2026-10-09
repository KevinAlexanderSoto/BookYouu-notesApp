package com.kalex.bookyouu_notesapp.camera.domain.usecase

import android.graphics.Bitmap
import com.kalex.bookyouu_notesapp.camera.domain.engine.ReceiptOcrEngine
import com.kalex.bookyouu_notesapp.camera.domain.model.ScannedReceiptData
import com.kalex.bookyouu_notesapp.camera.domain.parser.ReceiptTextParser

/**
 * Use case coordinating OCR text recognition on a captured bitmap and parsing
 * into a structured [ScannedReceiptData].
 */
class ScanReceiptUseCase(
    private val ocrEngine: ReceiptOcrEngine,
    private val textParser: ReceiptTextParser,
) {
    /**
     * Executes OCR on the image and extracts receipt data.
     *
     * @param bitmap Captured receipt image bitmap.
     * @return [Result] containing [ScannedReceiptData] on success or an exception on failure.
     */
    suspend operator fun invoke(bitmap: Bitmap): Result<ScannedReceiptData> {
        return try {
            val ocrResult = ocrEngine.recognizeText(bitmap)
            ocrResult.map { rawText ->
                textParser.parseReceipt(rawText)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
