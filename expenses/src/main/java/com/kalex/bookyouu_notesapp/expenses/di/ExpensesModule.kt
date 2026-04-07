package com.kalex.bookyouu_notesapp.expenses.di

import com.kalex.bookyouu_notesapp.db.BookYouuDataBase
import com.kalex.bookyouu_notesapp.expenses.data.repository.RoomExpenseRepository
import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository
import com.kalex.bookyouu_notesapp.expenses.domain.usecase.*
import com.kalex.bookyouu_notesapp.expenses.presentation.ExpenseViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val expensesModule = module {
    single { get<BookYouuDataBase>().expenseDao }
    singleOf(::RoomExpenseRepository) { bind<ExpenseRepository>() }
    singleOf(::GetMonthlyExpensesUseCase)
    singleOf(::GetMonthlySummaryUseCase)
    singleOf(::AddExpenseUseCase)
    singleOf(::DeleteExpenseUseCase)
    viewModelOf(::ExpenseViewModel)
}
