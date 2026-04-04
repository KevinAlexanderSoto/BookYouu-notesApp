package com.kalex.bookyouu_notesapp.payments.presentation

import com.kalex.bookyouu_notesapp.payments.domain.model.ObligationCategory
import com.kalex.bookyouu_notesapp.payments.domain.model.PaymentFrequency

data class ObligationsCreateState(
    val name: String = "",
    val amount: String = "",
    val frequency: PaymentFrequency = PaymentFrequency.MONTHLY,
    val dayOfMonth: Int = 1,
    val category: ObligationCategory? = null,
    val isLoading: Boolean = false,
    val isSaveEnabled: Boolean = false
)

sealed interface ObligationsCreateAction {
    data class OnNameChange(val name: String) : ObligationsCreateAction
    data class OnAmountChange(val amount: String) : ObligationsCreateAction
    data class OnFrequencyChange(val frequency: PaymentFrequency) : ObligationsCreateAction
    data class OnDayChange(val day: Int) : ObligationsCreateAction
    data class OnCategoryChange(val category: ObligationCategory) : ObligationsCreateAction
    data object OnSaveClick : ObligationsCreateAction
}

sealed interface ObligationsCreateEvent {
    data object ObligationSaved : ObligationsCreateEvent
    data class ShowError(val error: String) : ObligationsCreateEvent // Using String for now until UiText is found/added
}
