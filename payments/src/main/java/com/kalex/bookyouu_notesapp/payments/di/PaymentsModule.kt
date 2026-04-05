package com.kalex.bookyouu_notesapp.payments.di

import com.kalex.bookyouu_notesapp.payments.data.repository.RoomObligationRepository
import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository
import com.kalex.bookyouu_notesapp.payments.domain.usecase.AddObligationUseCase
import com.kalex.bookyouu_notesapp.payments.domain.usecase.DeleteObligationUseCase
import com.kalex.bookyouu_notesapp.payments.domain.usecase.GetObligationsUseCase
import com.kalex.bookyouu_notesapp.payments.domain.usecase.TogglePaymentUseCase
import com.kalex.bookyouu_notesapp.payments.presentation.ObligationsCreateViewModel
import com.kalex.bookyouu_notesapp.payments.presentation.ObligationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val paymentsModule = module {
    singleOf(::RoomObligationRepository) { bind<ObligationRepository>() }
    singleOf(::GetObligationsUseCase)
    singleOf(::AddObligationUseCase)
    singleOf(::TogglePaymentUseCase)
    singleOf(::DeleteObligationUseCase)
    viewModelOf(::ObligationsViewModel)
    viewModelOf(::ObligationsCreateViewModel)
}
