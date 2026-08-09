package com.kalex.bookyouu_notesapp.investments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import com.kalex.bookyouu_notesapp.investments.domain.repository.InvestmentsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class InvestmentListViewModel(
    private val investmentsRepositoryImpl: InvestmentsRepository
) : ViewModel() {

    private val amountFormatter = DecimalFormat("#,##0.00")
    private val percentFormatter = DecimalFormat("0.0")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

    private val _state = MutableStateFlow(PortfolioState())
    val state = _state.asStateFlow()

    private val _events = Channel<InvestmentsEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadPortfolio()
    }

    private fun loadPortfolio() {

        investmentsRepositoryImpl.getInvestments() .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { summary ->
                _state.update { state ->
                    state.copy(
                        totalNetWorth = summary.sumOf { it.initialAmount }.toString(),
                        investments = summary.map { it.toUiModel() },
                        isLoading = false
                    )
                }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = UiText.DynamicString(e.message ?: "Unknown Error")) }
            }
            .launchIn(viewModelScope)

    }

    fun onAction(action: InvestmentsAction) {
        when (action) {
            InvestmentsAction.LoadPortfolio -> loadPortfolio()
            is InvestmentsAction.OnInvestmentClick -> {
                // Detail screen is not implemented/needed yet
            }
            InvestmentsAction.OnAddInvestmentClick -> {
                viewModelScope.launch {
                    _events.send(InvestmentsEvent.NavigateToAddInvestment)
                }
            }
            is InvestmentsAction.OnDeleteInvestment -> {
                // TODO: Implement delete
            }
        }
    }

    private fun Investment.toUiModel(): InvestmentUi {
        val date = Instant.ofEpochMilli(dateCreated)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)

        return InvestmentUi(
            id = id,
            name = name,
            type = type,
            balance = "$ ${amountFormatter.format(initialAmount)}",
            typeLabel = type.name,
            dateCreated = date
        )
    }
}
