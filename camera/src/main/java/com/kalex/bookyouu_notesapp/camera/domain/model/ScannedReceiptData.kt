package com.kalex.bookyouu_notesapp.camera.domain.model

import java.time.LocalDate

/**
 * Represents structured financial and metadata extracted from a scanned receipt or invoice.
 *
 * @param merchantName Store, supermarket, restaurant, or business name detected from receipt header.
 * @param totalAmount Total grand amount paid, parsed from keywords (e.g. TOTAL, VALOR) or highest monetary value.
 * @param date Transaction or issue date detected from receipt lines.
 * @param rawText Complete unparsed raw text returned by the OCR engine.
 * @param confidence Confidence score of the extraction (default 1.0f).
 */
data class ScannedReceiptData(
    val merchantName: String? = null,
    val totalAmount: Double? = null,
    val date: LocalDate? = null,
    val rawText: String = "",
    val confidence: Float = 1.0f
)
