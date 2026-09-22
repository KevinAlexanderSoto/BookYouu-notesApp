package com.kalex.bookyouu_notesapp.expenses.presentation.pdf

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.kalex.bookyouu_notesapp.expenses.presentation.ExpenseUi
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ExpensePdfGenerator(private val context: Context) : MonthlyReportPdfExporter {

    private val primaryColor = Color.parseColor("#004D40")
    private val textDarkColor = Color.parseColor("#1A202C")
    private val textMutedColor = Color.parseColor("#4A5568")
    private val dividerColor = Color.parseColor("#CBD5E1")
    private val tableHeaderBg = Color.parseColor("#ECEFF1")
    private val altRowBg = Color.parseColor("#F8FAFC")

    override fun generateMonthlyReport(
        monthYear: String,
        totalSpent: String,
        expenses: List<ExpenseUi>
    ): Uri {
        val pdfDocument = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        var pageNumber = 1

        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // 1. Draw Header Banner
        paint.color = primaryColor
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), 100f, paint)

        paint.color = Color.WHITE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 22f
        canvas.drawText("BookYouu Notes App", 40f, 45f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 14f
        canvas.drawText("Monthly Expense Report • $monthYear", 40f, 72f, paint)

        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
        paint.textSize = 11f
        canvas.drawText("Generated on: $currentDate", (pageWidth - 190).toFloat(), 72f, paint)

        var yPos = 130f

        // 2. Metrics Summary Card
        paint.color = primaryColor
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 15f
        canvas.drawText("Financial Overview", 40f, yPos, paint)

        yPos += 15f
        paint.color = dividerColor
        paint.strokeWidth = 1f
        canvas.drawLine(40f, yPos, (pageWidth - 40).toFloat(), yPos, paint)

        yPos += 25f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = textMutedColor
        paint.textSize = 12f
        canvas.drawText("Total Spent: ", 40f, yPos, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = primaryColor
        paint.textSize = 14f
        canvas.drawText(totalSpent, 115f, yPos, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = textMutedColor
        paint.textSize = 12f
        canvas.drawText("Total Transactions: ", 300f, yPos, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = textDarkColor
        paint.textSize = 13f
        canvas.drawText("${expenses.size}", 420f, yPos, paint)

        yPos += 35f

        // 3. Expenses Ledger Table Header
        paint.color = primaryColor
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 14f
        canvas.drawText("Itemized Expenses", 40f, yPos, paint)
        yPos += 12f

        fun drawTableHeader(c: android.graphics.Canvas, currentY: Float) {
            paint.color = tableHeaderBg
            c.drawRect(40f, currentY, (pageWidth - 40).toFloat(), currentY + 24f, paint)

            paint.color = textDarkColor
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 10.5f
            c.drawText("Date", 48f, currentY + 16f, paint)
            c.drawText("Category", 145f, currentY + 16f, paint)
            c.drawText("Description", 255f, currentY + 16f, paint)
            c.drawText("Amount", 460f, currentY + 16f, paint)
        }

        drawTableHeader(canvas, yPos)
        yPos += 24f

        // 4. Draw Table Rows
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 10f

        for ((index, expense) in expenses.withIndex()) {
            if (yPos > pageHeight - 60) {
                // Finish current page and start a new one
                drawFooter(canvas, pageNumber, pageWidth, pageHeight, paint)
                pdfDocument.finishPage(page)

                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                yPos = 40f

                drawTableHeader(canvas, yPos)
                yPos += 24f
            }

            if (index % 2 == 1) {
                paint.color = altRowBg
                canvas.drawRect(40f, yPos, (pageWidth - 40).toFloat(), yPos + 22f, paint)
            }

            paint.color = textDarkColor
            canvas.drawText(expense.date, 48f, yPos + 15f, paint)
            val categoryName = expense.category.name().lowercase().replaceFirstChar { it.uppercase() }
            canvas.drawText(categoryName, 145f, yPos + 15f, paint)

            val descTruncated = if (expense.description.length > 30) expense.description.take(28) + "..." else expense.description
            canvas.drawText(descTruncated, 255f, yPos + 15f, paint)

            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = primaryColor
            canvas.drawText(expense.amount, 460f, yPos + 15f, paint)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

            yPos += 22f
        }

        drawFooter(canvas, pageNumber, pageWidth, pageHeight, paint)
        pdfDocument.finishPage(page)

        // Save PDF file to cacheDir/reports/
        val reportsDir = File(context.cacheDir, "reports").apply { if (!exists()) mkdirs() }
        val pdfFile = File(reportsDir, "BookYouu_Expenses_${monthYear.replace("-", "_")}.pdf")
        FileOutputStream(pdfFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.expenses.fileprovider",
            pdfFile
        )
    }

    private fun drawFooter(canvas: android.graphics.Canvas, pageNum: Int, width: Int, height: Int, paint: Paint) {
        paint.color = dividerColor
        paint.strokeWidth = 0.8f
        canvas.drawLine(40f, (height - 40).toFloat(), (width - 40).toFloat(), (height - 40).toFloat(), paint)

        paint.color = textMutedColor
        paint.textSize = 9f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("BookYouu App • Monthly Expense Report", 40f, (height - 25).toFloat(), paint)
        canvas.drawText("Page $pageNum", (width - 80).toFloat(), (height - 25).toFloat(), paint)
    }
}
