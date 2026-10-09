package com.kalex.bookyouu_notesapp.camera.data.parser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class RegexReceiptTextParserTest {

    private lateinit var parser: RegexReceiptTextParser

    @Before
    fun setUp() {
        parser = RegexReceiptTextParser()
    }

    @Test
    fun `parses Colombian supermarket receipt with dot thousands and DD-MM-YYYY date`() {
        val receiptText = """
            ALMACENES EXITO S.A.S
            NIT 890.900.608-9
            AUTORIZACION DIAN 18760000001
            FECHA: 15/09/2026 14:30
            CAJERO: JUAN PEREZ
            1 LECHE DESLACTOSADA 4.200
            1 PAN TAJADO 2.500
            TOTAL A PAGAR: $ 6.700
            FORMA DE PAGO: TARJETA
        """.trimIndent()

        val result = parser.parseReceipt(receiptText)

        assertEquals("Almacenes Exito", result.merchantName)
        assertEquals(6700.0, result.totalAmount ?: 0.0, 0.001)
        assertEquals(LocalDate.of(2026, 9, 15), result.date)
        assertTrue(result.confidence >= 0.8f)
    }

    @Test
    fun `parses US receipt with decimals and ISO date`() {
        val receiptText = """
            WALMART SUPERCENTER
            STORE #1234
            DATE: 2026-08-12 11:45 AM
            1 APPLES 3.49
            1 BREAD 2.19
            TAX: 0.52
            TOTAL: $ 6.20
            CASH: 10.00
            CHANGE: 3.80
        """.trimIndent()

        val result = parser.parseReceipt(receiptText)

        assertEquals("Walmart Supercenter", result.merchantName)
        assertEquals(6.20, result.totalAmount ?: 0.0, 0.001)
        assertEquals(LocalDate.of(2026, 8, 12), result.date)
    }

    @Test
    fun `parses receipt with Spanish text month and Valor Total`() {
        val receiptText = """
            RESTAURANTE EL PORTON
            04 de Octubre de 2026
            MESA 5
            CONSUMO ALIMENTOS 45.000
            VALOR TOTAL: 45.000
            GRACIAS POR SU COMPRA
        """.trimIndent()

        val result = parser.parseReceipt(receiptText)

        assertEquals("Restaurante El Porton", result.merchantName)
        assertEquals(45000.0, result.totalAmount ?: 0.0, 0.001)
        assertEquals(LocalDate.of(2026, 10, 4), result.date)
    }

    @Test
    fun `distinguishes between subtotal, propina, and total`() {
        val receiptText = """
            CAFE COLOMBIANO
            FECHA: 01/10/2026
            SUBTOTAL: $ 20.000
            IVA 19%: $ 3.800
            PROPINA VOLUNTARIA: $ 2.380
            TOTAL: $ 26.180
        """.trimIndent()

        val result = parser.parseReceipt(receiptText)

        assertEquals("Cafe Colombiano", result.merchantName)
        assertEquals(26180.0, result.totalAmount ?: 0.0, 0.001)
        assertEquals(LocalDate.of(2026, 10, 1), result.date)
    }

    @Test
    fun `fallback to largest number when no total keyword exists`() {
        val receiptText = """
            FARMACIA SAN PABLO
            2026-05-20
            MEDICAMENTO A 12.000
            MEDICAMENTO B 18.000
            30.000
        """.trimIndent()

        val result = parser.parseReceipt(receiptText)

        assertEquals("Farmacia San Pablo", result.merchantName)
        assertEquals(30000.0, result.totalAmount ?: 0.0, 0.001)
        assertEquals(LocalDate.of(2026, 5, 20), result.date)
    }

    @Test
    fun `returns null fields for empty or blank text`() {
        val result = parser.parseReceipt("")

        assertNull(result.merchantName)
        assertNull(result.totalAmount)
        assertNull(result.date)
        assertEquals(0.0f, result.confidence, 0.001f)
    }

    @Test
    fun `parseNumericAmount handles varying thousand and decimal formats`() {
        assertEquals(12500.0, parser.parseNumericAmount("$ 12.500") ?: 0.0, 0.001)
        assertEquals(12.50, parser.parseNumericAmount("$ 12.50") ?: 0.0, 0.001)
        assertEquals(12500.50, parser.parseNumericAmount("12,500.50") ?: 0.0, 0.001)
        assertEquals(12500.50, parser.parseNumericAmount("12.500,50") ?: 0.0, 0.001)
        assertEquals(1000000.0, parser.parseNumericAmount("1.000.000") ?: 0.0, 0.001)
        assertEquals(550.0, parser.parseNumericAmount("550") ?: 0.0, 0.001)
    }
}
