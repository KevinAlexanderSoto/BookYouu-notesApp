package com.kalex.bookyouu_notesapp.camera.domain.usecase

import android.graphics.Bitmap
import com.kalex.bookyouu_notesapp.camera.domain.engine.ReceiptOcrEngine
import com.kalex.bookyouu_notesapp.camera.domain.model.ScannedReceiptData
import com.kalex.bookyouu_notesapp.camera.domain.parser.ReceiptTextParser
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ScanReceiptUseCaseTest {

    private fun createFakeBitmap(): Bitmap {
        val unsafeClass = Class.forName("sun.misc.Unsafe")
        val field = unsafeClass.getDeclaredField("theUnsafe")
        field.isAccessible = true
        val unsafe = field.get(null)
        val allocateMethod = unsafeClass.getMethod("allocateInstance", Class::class.java)
        return allocateMethod.invoke(unsafe, Bitmap::class.java) as Bitmap
    }

    private class FakeReceiptOcrEngine(
        var result: Result<String> = Result.success("Sample text")
    ) : ReceiptOcrEngine {
        override suspend fun recognizeText(bitmap: Bitmap): Result<String> = result
    }

    private class FakeReceiptTextParser(
        var parsedData: ScannedReceiptData = ScannedReceiptData()
    ) : ReceiptTextParser {
        var shouldThrow: Boolean = false
        override fun parseReceipt(rawText: String): ScannedReceiptData {
            if (shouldThrow) throw RuntimeException("Parsing error")
            return parsedData
        }
    }

    @Test
    fun `when OCR succeeds, returns parsed receipt data`() = runBlocking {
        // Given
        val expectedData = ScannedReceiptData(
            merchantName = "Starbucks",
            totalAmount = 24.50,
            date = LocalDate.of(2026, 10, 4),
            rawText = "STARBUCKS COFFEE\nTOTAL: $24.50\n04/10/2026"
        )
        val ocrEngine = FakeReceiptOcrEngine(Result.success(expectedData.rawText))
        val textParser = FakeReceiptTextParser(expectedData)
        val useCase = ScanReceiptUseCase(ocrEngine, textParser)

        // When
        val fakeBitmap = createFakeBitmap()
        val result = useCase(fakeBitmap)

        // Then
        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertEquals("Starbucks", data?.merchantName)
        assertEquals(24.50, data?.totalAmount ?: 0.0, 0.001)
        assertEquals(LocalDate.of(2026, 10, 4), data?.date)
    }

    @Test
    fun `when OCR fails, returns failure result`() = runBlocking {
        // Given
        val ocrEngine = FakeReceiptOcrEngine(Result.failure(Exception("OCR unreadable image")))
        val textParser = FakeReceiptTextParser()
        val useCase = ScanReceiptUseCase(ocrEngine, textParser)

        // When
        val fakeBitmap = createFakeBitmap()
        val result = useCase(fakeBitmap)

        // Then
        assertTrue(result.isFailure)
        assertEquals("OCR unreadable image", result.exceptionOrNull()?.message)
    }

    @Test
    fun `when parser throws exception, returns failure result`() = runBlocking {
        // Given
        val ocrEngine = FakeReceiptOcrEngine(Result.success("Some raw text"))
        val textParser = FakeReceiptTextParser().apply { shouldThrow = true }
        val useCase = ScanReceiptUseCase(ocrEngine, textParser)

        // When
        val fakeBitmap = createFakeBitmap()
        val result = useCase(fakeBitmap)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Parsing error", result.exceptionOrNull()?.message)
    }
}
