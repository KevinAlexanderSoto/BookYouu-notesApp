package com.kalex.bookyouu_notesapp.authentication.di

import com.kalex.bookyouu_notesapp.authentication.BiometricSupportUseCase
import com.kalex.bookyouu_notesapp.authentication.FingerPrintAuthenticationViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authenticationModule = module {
    singleOf(::BiometricSupportUseCase)
    viewModelOf(::FingerPrintAuthenticationViewModel)
}
