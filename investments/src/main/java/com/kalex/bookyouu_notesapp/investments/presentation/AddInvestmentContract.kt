package com.kalex.bookyouu_notesapp.investments.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.investments.R

enum class Currency(
    val code: String,
    val symbol: String,
    @StringRes val titleResId: Int
) {
    USD("USD", "$", R.string.currency_usd),
    COP("COP", "$", R.string.currency_cop)
}

data class AddInvestmentState(
    val investmentId: Long? = null,
    val isEditMode: Boolean = false,
    val amount: String = "0",
    val name: String = "",
    val selectedType: InvestmentType = InvestmentType.GENERAL,
    val selectedCurrency: Currency = Currency.USD,
    val term: String = "",
    val annualRevenue: String = "",
    val isLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val error: UiText? = null
)

sealed interface AddInvestmentAction {
    data class LoadInvestment(val id: Long) : AddInvestmentAction
    data class OnAmountChange(val amount: String) : AddInvestmentAction
    data class OnNameChange(val name: String) : AddInvestmentAction
    data class OnTypeChange(val type: InvestmentType) : AddInvestmentAction
    data class OnCurrencyChange(val currency: Currency) : AddInvestmentAction
    data class OnTermChange(val term: String) : AddInvestmentAction
    data class OnRevenueChange(val revenue: String) : AddInvestmentAction
    object OnCreateInvestment : AddInvestmentAction
    object OnDeleteClick : AddInvestmentAction
    object OnConfirmDelete : AddInvestmentAction
    object OnDismissDeleteDialog : AddInvestmentAction
}

sealed interface AddInvestmentEvent {
    object InvestmentCreated : AddInvestmentEvent
    object InvestmentDeleted : AddInvestmentEvent
    data class ShowError(val message: UiText) : AddInvestmentEvent
}

enum class InvestmentTermPreset(val days: Int?) {
    NO_TERM(null),
    DAYS_30(30),
    DAYS_90(90),
    DAYS_180(180),
    DAYS_360(360);

    val valueString: String
        get() = days?.toString() ?: ""
}

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH
}

const val NON_INVESTMENT_ID = -1L

enum class InvestmentType(
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
    val riskLevel: RiskLevel
) {

    HIGH_YIELD_SAVINGS(
        titleResId = R.string.investment_high_yield_savings,
        iconResId = R.drawable.ic_high_yield_savings,
        riskLevel = RiskLevel.LOW
    ),
    CDT(
        titleResId = R.string.investment_cdt,
        iconResId = R.drawable.ic_cdt,
        riskLevel = RiskLevel.LOW
    ),
    USD(
        titleResId = R.string.investment_usd,
        iconResId = R.drawable.ic_usd,
        riskLevel = RiskLevel.MEDIUM
    ),
    STOCKS(
        titleResId = R.string.investment_stocks,
        iconResId = R.drawable.ic_stocks,
        riskLevel = RiskLevel.HIGH
    ),
    BONDS(
        titleResId = R.string.investment_bonds,
        iconResId = R.drawable.ic_bonds,
        riskLevel = RiskLevel.LOW
    ),
    MUTUAL_FUNDS(
        titleResId = R.string.investment_mutual_funds,
        iconResId = R.drawable.ic_mutual_funds,
        riskLevel = RiskLevel.MEDIUM
    ),
    ETF(
        titleResId = R.string.investment_etf,
        iconResId = R.drawable.ic_etf,
        riskLevel = RiskLevel.MEDIUM
    ),
    REAL_ESTATE(
        titleResId = R.string.investment_real_estate,
        iconResId = R.drawable.ic_real_estate,
        riskLevel = RiskLevel.MEDIUM
    ),
    CRYPTO(
        titleResId = R.string.investment_crypto,
        iconResId = R.drawable.ic_crypto,
        riskLevel = RiskLevel.VERY_HIGH
    ),
    GENERAL(
        titleResId = R.string.investment_general,
        iconResId = R.drawable.ic_general,
        riskLevel = RiskLevel.LOW
    )
}
