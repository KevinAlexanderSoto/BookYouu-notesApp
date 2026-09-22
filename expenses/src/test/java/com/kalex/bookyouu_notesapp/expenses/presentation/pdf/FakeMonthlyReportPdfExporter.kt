package com.kalex.bookyouu_notesapp.expenses.presentation.pdf

import android.net.Uri
import com.kalex.bookyouu_notesapp.expenses.presentation.ExpenseUi

class FakeMonthlyReportPdfExporter : MonthlyReportPdfExporter {

    var generateCallCount = 0
    var lastMonthYear: String? = null

    override fun generateMonthlyReport(
        monthYear: String,
        totalSpent: String,
        expenses: List<ExpenseUi>
    ): Uri {
        generateCallCount++
        lastMonthYear = monthYear
        // Return a mock/empty Uri for JVM tests
        return Uri.EMPTY
    }
}
