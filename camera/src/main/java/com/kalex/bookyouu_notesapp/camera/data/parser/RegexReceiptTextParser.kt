package com.kalex.bookyouu_notesapp.camera.data.parser

import com.kalex.bookyouu_notesapp.camera.domain.model.ScannedReceiptData
import com.kalex.bookyouu_notesapp.camera.domain.parser.ReceiptTextParser
import java.time.LocalDate

/**
 * Concrete implementation of [ReceiptTextParser] using heuristic rule matching
 * and regular expressions to extract merchant name, total price, and date from receipts.
 */
class RegexReceiptTextParser : ReceiptTextParser {

    override fun parseReceipt(rawText: String): ScannedReceiptData {
        if (rawText.isBlank()) {
            return ScannedReceiptData(rawText = rawText, confidence = 0.0f)
        }

        val lines = rawText.lines().map { it.trim() }.filter { it.isNotEmpty() }

        val merchantName = extractMerchantName(lines)
        val totalAmount = extractTotalAmount(lines)
        val date = extractDate(lines)

        return ScannedReceiptData(
            merchantName = merchantName,
            totalAmount = totalAmount,
            date = date,
            rawText = rawText,
            confidence = calculateConfidence(merchantName, totalAmount, date)
        )
    }

    private fun extractMerchantName(lines: List<String>): String? {
        val noiseKeywords = setOf(
            "FACTURA", "TICKET", "RECIBO", "SIMPLIFICADO", "ELECTRONICA", "RESOLUCION",
            "AUTORIZACION", "DIAN", "REGIMEN", "COMUN", "NIT", "RUT", "RUC", "IVA",
            "TEL", "TELEFONO", "CEL", "DIR", "DIRECCION", "CALLE", "CRA", "CARRERA",
            "AV", "AVENIDA", "AUTOPISTA", "CAJERO", "CAJA", "POS", "TERMINAL",
            "FECHA", "HORA", "DATE", "TIME", "PAGINA", "PAGE", "WWW", "HTTP", "HTTPS",
            ".COM", ".CO", "BIENVENIDO", "GRACIAS", "THANK", "CLIENTE", "CUSTOMER",
            "COPIA", "ORIGINAL", "ORDEN", "MESA", "TURNO"
        )

        // Inspect the first 5 lines (header area)
        for (line in lines.take(5)) {
            val upperLine = line.uppercase()

            // Skip lines that contain obvious non-merchant keywords
            val containsNoise = noiseKeywords.any { noise ->
                upperLine.split(Regex("[^A-Z0-9]+")).contains(noise)
            }
            if (containsNoise) continue

            // Skip lines that have no letters or are too short
            val letterCount = line.count { it.isLetter() }
            if (letterCount < 3) continue

            // Clean line
            val cleaned = cleanMerchantString(line)
            if (cleaned.isNotBlank() && cleaned.length >= 3) {
                return cleaned.take(50)
            }
        }

        return null
    }

    private fun cleanMerchantString(raw: String): String {
        // Strip common store legal suffixes or punctuation noise
        var cleaned = raw
            .replace(Regex("""[*#=_~|<>]+"""), "")
            .replace(Regex("""(?i)\b(S\.?A\.?S?|LTDA|INC|LLC|CORP|S\.?L\.?)\b"""), "")
            .trim(' ', '-', ':', '.', ',')

        // If all uppercase, convert to Title Case for better readability
        if (cleaned.isNotEmpty() && cleaned == cleaned.uppercase()) {
            cleaned = cleaned.lowercase().split(" ")
                .filter { it.isNotEmpty() }
                .joinToString(" ") { word ->
                    word.replaceFirstChar { it.uppercase() }
                }
        }

        return cleaned
    }

    private fun extractTotalAmount(lines: List<String>): Double? {
        // Priority 1: High confidence phrases (TOTAL A PAGAR, VALOR TOTAL, etc.)
        val strongTotalRegex = Regex(
            """(?i)(?:TOTAL\s+A\s+PAGAR|TOTAL\s+PAGAR|VALOR\s+TOTAL|VALOR\s+A\s+PAGAR|IMPORTE\s+TOTAL|GRAN\s+TOTAL|GRAND\s+TOTAL|BALANCE\s+DUE|AMOUNT\s+DUE)[\s:=]*[$€£]?\s*([0-9]{1,3}(?:[.,][0-9]{3})+(?:[.,][0-9]{1,2})|[0-9]{1,3}(?:[.,][0-9]{3})+|[0-9]+(?:[.,][0-9]{1,2})?)\b"""
        )

        for (line in lines.reversed()) {
            val match = strongTotalRegex.find(line)
            if (match != null) {
                val amountStr = match.groupValues[1]
                val parsed = parseNumericAmount(amountStr)
                if (parsed != null && parsed > 0.0) return parsed
            }
        }

        // Priority 2: Standard keywords (TOTAL, VALOR, IMPORTE, NETO, PAGAR)
        // Ensure "SUBTOTAL", "IVA", "PROPINA", "DESCUENTO", "CAMBIO" are excluded
        val standardTotalRegex = Regex(
            """(?i)(?<!SUB)(?<!CON\s)(?<!SIN\s)\b(TOTAL|VALOR|IMPORTE|NETO|PAGAR)\b[\s:=]*[$€£]?\s*([0-9]{1,3}(?:[.,][0-9]{3})+(?:[.,][0-9]{1,2})|[0-9]{1,3}(?:[.,][0-9]{3})+|[0-9]+(?:[.,][0-9]{1,2})?)\b"""
        )

        for (line in lines.reversed()) {
            // Discard lines that contain tax/subtotal/tip keywords alongside total
            val upper = line.uppercase()
            if (upper.contains("SUBTOTAL") || upper.contains("PROPINA") || upper.contains("DESCUENTO") || upper.contains("CAMBIO")) {
                continue
            }

            val match = standardTotalRegex.find(line)
            if (match != null) {
                val amountStr = match.groupValues[2]
                val parsed = parseNumericAmount(amountStr)
                if (parsed != null && parsed > 0.0) return parsed
            }
        }

        // Priority 3: Fallback - Largest plausible monetary number in bottom half of receipt
        val numberRegex = Regex(
            """[$€£]?\s*([0-9]{1,3}(?:[.,][0-9]{3})+(?:[.,][0-9]{1,2})|[0-9]{1,3}(?:[.,][0-9]{3})+|[0-9]+[.,][0-9]{1,2})\b"""
        )
        val bottomHalfLines = lines.takeLast((lines.size * 0.6).toInt().coerceAtLeast(1))
        var maxAmount: Double? = null

        for (line in bottomHalfLines) {
            val upper = line.uppercase()
            if (upper.contains("TEL") || upper.contains("NIT") || upper.contains("FECHA") || upper.contains("HORA")) continue

            for (match in numberRegex.findAll(line)) {
                val parsed = parseNumericAmount(match.groupValues[1])
                if (parsed != null && parsed > 0.0) {
                    if (maxAmount == null || parsed > maxAmount) {
                        maxAmount = parsed
                    }
                }
            }
        }

        return maxAmount
    }

    internal fun parseNumericAmount(raw: String): Double? {
        var cleaned = raw.replace(Regex("""[^0-9.,]"""), "").trim()
        if (cleaned.isEmpty()) return null

        // Format containing both dot and comma (e.g. 1,234.56 or 1.234,56)
        if (cleaned.contains('.') && cleaned.contains(',')) {
            val lastDot = cleaned.lastIndexOf('.')
            val lastComma = cleaned.lastIndexOf(',')
            cleaned = if (lastDot > lastComma) {
                // US style: 1,234.56 -> remove commas
                cleaned.replace(",", "")
            } else {
                // EU / Latin style: 1.234,56 -> remove dots, replace comma with dot
                cleaned.replace(".", "").replace(",", ".")
            }
        } else if (cleaned.contains('.')) {
            val parts = cleaned.split('.')
            if (parts.size > 2) {
                // Multiple dots e.g. 1.000.000
                cleaned = cleaned.replace(".", "")
            } else if (parts.size == 2) {
                val decimalPart = parts[1]
                if (decimalPart.length == 3) {
                    // Dot used as thousands separator e.g. 12.500 or 4.200
                    cleaned = cleaned.replace(".", "")
                }
            }
        } else if (cleaned.contains(',')) {
            val parts = cleaned.split(',')
            if (parts.size > 2) {
                // Multiple commas e.g. 1,000,000
                cleaned = cleaned.replace(",", "")
            } else if (parts.size == 2) {
                val decimalPart = parts[1]
                if (decimalPart.length == 3) {
                    // Comma used as thousands separator e.g. 12,500
                    cleaned = cleaned.replace(",", "")
                } else {
                    // Comma used as decimal separator e.g. 12,50
                    cleaned = cleaned.replace(",", ".")
                }
            }
        }

        return cleaned.toDoubleOrNull()
    }

    private fun extractDate(lines: List<String>): LocalDate? {
        // Pattern 1: ISO or slash YYYY-MM-DD or YYYY/MM/DD
        val isoRegex = Regex("""\b(20\d{2})[-/.](0?[1-9]|1[0-2])[-/.](0?[1-9]|[12]\d|3[01])\b""")

        // Pattern 2: DD/MM/YYYY or DD-MM-YYYY or DD.MM.YYYY
        val dmyRegex = Regex("""\b(0?[1-9]|[12]\d|3[01])[-/.](0?[1-9]|1[0-2])[-/.](20\d{2})\b""")

        // Pattern 3: Alphanumeric month e.g. "15 de Octubre de 2026" or "15 OCT 2026" or "15-Oct-2026"
        val alphaMonthRegex = Regex(
            """(?i)\b(0?[1-9]|[12]\d|3[01])\s*(?:de\s+|-)?([a-záéíóú]{3,10})\.?,?\s*(?:del?\s+|-)?(20\d{2})\b"""
        )

        for (line in lines) {
            // Check Pattern 1: ISO YYYY-MM-DD
            val isoMatch = isoRegex.find(line)
            if (isoMatch != null) {
                val year = isoMatch.groupValues[1].toInt()
                val month = isoMatch.groupValues[2].toInt()
                val day = isoMatch.groupValues[3].toInt()
                val date = safeLocalDate(year, month, day)
                if (date != null) return date
            }

            // Check Pattern 2: DD/MM/YYYY
            val dmyMatch = dmyRegex.find(line)
            if (dmyMatch != null) {
                val day = dmyMatch.groupValues[1].toInt()
                val month = dmyMatch.groupValues[2].toInt()
                val year = dmyMatch.groupValues[3].toInt()
                val date = safeLocalDate(year, month, day)
                if (date != null) return date
            }

            // Check Pattern 3: DD MMMM YYYY
            val alphaMatch = alphaMonthRegex.find(line)
            if (alphaMatch != null) {
                val day = alphaMatch.groupValues[1].toInt()
                val monthStr = alphaMatch.groupValues[2].lowercase()
                val year = alphaMatch.groupValues[3].toInt()
                val month = parseMonthName(monthStr)
                if (month != null) {
                    val date = safeLocalDate(year, month, day)
                    if (date != null) return date
                }
            }
        }

        return null
    }

    private fun safeLocalDate(year: Int, month: Int, day: Int): LocalDate? {
        return try {
            LocalDate.of(year, month, day)
        } catch (_: Exception) {
            null
        }
    }

    private fun parseMonthName(rawMonth: String): Int? {
        val normalized = rawMonth.trim().lowercase()
        return when {
            normalized.startsWith("ene") || normalized.startsWith("jan") -> 1
            normalized.startsWith("feb") -> 2
            normalized.startsWith("mar") -> 3
            normalized.startsWith("abr") || normalized.startsWith("apr") -> 4
            normalized.startsWith("may") -> 5
            normalized.startsWith("jun") -> 6
            normalized.startsWith("jul") -> 7
            normalized.startsWith("ago") || normalized.startsWith("aug") -> 8
            normalized.startsWith("sep") || normalized.startsWith("set") -> 9
            normalized.startsWith("oct") -> 10
            normalized.startsWith("nov") -> 11
            normalized.startsWith("dic") || normalized.startsWith("dec") -> 12
            else -> null
        }
    }

    private fun calculateConfidence(merchant: String?, amount: Double?, date: LocalDate?): Float {
        var score = 0.0f
        if (!merchant.isNullOrBlank()) score += 0.35f
        if (amount != null && amount > 0.0) score += 0.45f
        if (date != null) score += 0.20f
        return score
    }
}
