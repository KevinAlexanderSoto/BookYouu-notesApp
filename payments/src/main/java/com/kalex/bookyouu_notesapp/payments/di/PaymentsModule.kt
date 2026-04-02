package com.kalex.bookyouu_notesapp.payments.di

import com.kalex.bookyouu_notesapp.payments.data.repository.RoomObligationRepository
import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository
import com.kalex.bookyouu_notesapp.payments.domain.usecase.GetObligationsUseCase
import com.kalex.bookyouu_notesapp.payments.domain.usecase.TogglePaymentUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val paymentsModule = module {
    singleOf(::RoomObligationRepository) { bind<ObligationRepository>() }
    singleOf(::GetObligationsUseCase)
    singleOf(::TogglePaymentUseCase)
}
