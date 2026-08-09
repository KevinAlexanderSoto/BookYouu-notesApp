package com.kalex.bookyouu_notesapp.investments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import com.kalex.bookyouu_notesapp.investments.domain.repository.InvestmentsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import com.kalex.bookyouu_notesapp.investments.R

class AddInvestmentViewModel(
    private val repository: InvestmentsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddInvestmentState())
    val state = _state.asStateFlow()

    private val _events = Channel<AddInvestmentEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: AddInvestmentAction) {
        when (action) {
            is AddInvestmentAction.OnAmountChange -> {
                _state.update { it.copy(amount = action.amount) }
            }
            is AddInvestmentAction.OnNameChange -> {
                _state.update { it.copy(name = action.name) }
            }
            is AddInvestmentAction.OnTypeChange -> {
                _state.update { it.copy(selectedType = action.type) }
            }
            is AddInvestmentAction.OnTermChange -> {
                val newTerm = if (action.term.isBlank()) {
                    ""
                } else {
                    val digits = action.term.filter { it.isDigit() }
                    if (digits.isBlank()) ""
                    else {
                        val num = digits.toIntOrNull() ?: 0
                        when {
                            num > 360 -> "360"
                            num < 1 -> "1"
                            else -> num.toString()
                        }
                    }
                }
                _state.update { it.copy(term = newTerm) }
            }
            is AddInvestmentAction.OnRevenueChange -> {
                _state.update { it.copy(annualRevenue = action.revenue) }
            }
            AddInvestmentAction.OnCreateInvestment -> {
                createInvestment()
            }
        }
    }

    private fun createInvestment() {
        val currentState = _state.value
        if (currentState.name.isBlank()) {
            viewModelScope.launch {
                _events.send(AddInvestmentEvent.ShowError(UiText.StringResource(R.string.add_investment_error_empty_name)))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val investment = Investment(
                    name = currentState.name,
                    type = currentState.selectedType,
                    initialAmount = currentState.amount.toDoubleOrNull() ?: 0.0,
                    dateCreated = System.currentTimeMillis()
                )
                repository.upsertInvestment(investment)
                _events.send(AddInvestmentEvent.InvestmentCreated)
            } catch (e: Exception) {
                //TODO: Manage error
                _events.send(AddInvestmentEvent.ShowError(UiText.DynamicString(e.message ?: "Error saving")))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
