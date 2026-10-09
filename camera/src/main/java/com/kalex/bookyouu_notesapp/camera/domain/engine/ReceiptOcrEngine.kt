package com.kalex.bookyouu_notesapp.camera.domain.engine

import android.graphics.Bitmap

/**
 * Interface abstracting the OCR text recognition engine from image bitmaps.
 */
interface ReceiptOcrEngine {
    /**
     * Recognizes and extracts raw text from the provided image bitmap.
     *
     * @param bitmap The captured receipt image.
     * @return Result containing recognized text or error.
     */
    suspend fun recognizeText(bitmap: Bitmap): Result<String>
}
