package com.kalex.bookyouu_notesapp.camera.domain.parser

import com.kalex.bookyouu_notesapp.camera.domain.model.ScannedReceiptData

/**
 * Interface for parsing unstructured OCR receipt text into structured [ScannedReceiptData].
 */
interface ReceiptTextParser {
    /**
     * Parses raw OCR text into structured receipt fields: merchant name, total price, and date.
     *
     * @param rawText Unstructured text extracted by OCR.
     * @return Structured [ScannedReceiptData] containing parsed fields.
     */
    fun parseReceipt(rawText: String): ScannedReceiptData
}
