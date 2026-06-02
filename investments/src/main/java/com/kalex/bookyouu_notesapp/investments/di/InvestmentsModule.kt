package com.kalex.bookyouu_notesapp.investments.di

import com.kalex.bookyouu_notesapp.db.BookYouuDataBase
import com.kalex.bookyouu_notesapp.investments.data.repository.InvestmentsRepositoryImpl
import com.kalex.bookyouu_notesapp.investments.domain.repository.InvestmentsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val investmentsModule = module {
    single { get<BookYouuDataBase>().investmentDao }
    single { get<BookYouuDataBase>().investmentTransactionDao }
    singleOf(::InvestmentsRepositoryImpl) { bind<InvestmentsRepository>() }
}
