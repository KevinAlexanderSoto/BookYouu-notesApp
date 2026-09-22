package com.kalex.bookyouu_notesapp.expenses.presentation.pdf

import android.net.Uri
import com.kalex.bookyouu_notesapp.expenses.presentation.ExpenseUi

interface MonthlyReportPdfExporter {
    fun generateMonthlyReport(
        monthYear: String,
        totalSpent: String,
        expenses: List<ExpenseUi>
    ): Uri
}
