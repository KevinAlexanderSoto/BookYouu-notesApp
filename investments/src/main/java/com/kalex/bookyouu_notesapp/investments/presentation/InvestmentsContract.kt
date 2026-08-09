package com.kalex.bookyouu_notesapp.investments.presentation

import com.kalex.bookyouu_notesapp.core.common.UiText

data class InvestmentUi(
    val id: Long,
    val name: String,
    val type: InvestmentType,
    val balance: String,
    val typeLabel: String,
    val dateCreated: String
)

data class PortfolioState(
    val totalNetWorth: String = "$ 0.00",
    val investments: List<InvestmentUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface InvestmentsAction {
    object LoadPortfolio : InvestmentsAction
    data class OnInvestmentClick(val id: Long) : InvestmentsAction
    object OnAddInvestmentClick : InvestmentsAction
    data class OnDeleteInvestment(val investment: InvestmentUi) : InvestmentsAction
}

sealed interface InvestmentsEvent {
    object NavigateToAddInvestment : InvestmentsEvent
    data class ShowError(val message: UiText) : InvestmentsEvent
}
