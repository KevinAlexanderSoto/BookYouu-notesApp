package com.kalex.bookyouu_notesapp.investments.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.investments.R

data class AddInvestmentState(
    val amount: String = "0",
    val name: String = "",
    val selectedType: InvestmentType = InvestmentType.GENERAL,
    val term: String = "",
    val annualRevenue: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface AddInvestmentAction {
    data class OnAmountChange(val amount: String) : AddInvestmentAction
    data class OnNameChange(val name: String) : AddInvestmentAction
    data class OnTypeChange(val type: InvestmentType) : AddInvestmentAction
    data class OnTermChange(val term: String) : AddInvestmentAction
    data class OnRevenueChange(val revenue: String) : AddInvestmentAction
    object OnCreateInvestment : AddInvestmentAction
}

sealed interface
AddInvestmentEvent {
    object InvestmentCreated : AddInvestmentEvent
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

enum class InvestmentType(
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
    val riskLevel: RiskLevel
) {

    HIGH_YIELD_SAVINGS(
        titleResId = R.string.investment_high_yield_savings,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.LOW
    ),
    CDT(
        titleResId = R.string.investment_cdt,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.LOW
    ),
    USD(
        titleResId = R.string.investment_usd,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.MEDIUM
    ),
    STOCKS(
        titleResId = R.string.investment_stocks,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.HIGH
    ),
    BONDS(
        titleResId = R.string.investment_bonds,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.LOW
    ),
    MUTUAL_FUNDS(
        titleResId = R.string.investment_mutual_funds,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.MEDIUM
    ),
    ETF(
        titleResId = R.string.investment_etf,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.MEDIUM
    ),
    REAL_ESTATE(
        titleResId = R.string.investment_real_estate,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.MEDIUM
    ),
    CRYPTO(
        titleResId = R.string.investment_crypto,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.VERY_HIGH
    ),
    GENERAL(
        titleResId = R.string.investment_general,
        iconResId = R.drawable.outline_money_bag_24,
        riskLevel = RiskLevel.LOW
    )
}
