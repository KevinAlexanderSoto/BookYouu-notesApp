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

    private var existingDateCreated: Long? = null

    fun onAction(action: AddInvestmentAction) {
        when (action) {
            is AddInvestmentAction.LoadInvestment -> {
                loadInvestment(action.id)
            }
            is AddInvestmentAction.OnAmountChange -> {
                _state.update { it.copy(amount = action.amount) }
            }
            is AddInvestmentAction.OnNameChange -> {
                _state.update { it.copy(name = action.name) }
            }
            is AddInvestmentAction.OnTypeChange -> {
                _state.update { it.copy(selectedType = action.type) }
            }
            is AddInvestmentAction.OnCurrencyChange -> {
                _state.update { it.copy(selectedCurrency = action.currency) }
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
            AddInvestmentAction.OnDeleteClick -> {
                _state.update { it.copy(showDeleteDialog = true) }
            }
            AddInvestmentAction.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = false) }
            }
            AddInvestmentAction.OnConfirmDelete -> {
                deleteInvestment()
            }
        }
    }

    private fun loadInvestment(id: Long) {
        if (id <= 0) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val investment = repository.getInvestmentById(id).firstOrNull()
                if (investment != null) {
                    existingDateCreated = investment.dateCreated
                    val amountStr = if (investment.initialAmount % 1 == 0.0) {
                        investment.initialAmount.toLong().toString()
                    } else {
                        investment.initialAmount.toString()
                    }
                    _state.update {
                        it.copy(
                            investmentId = investment.id,
                            isEditMode = true,
                            name = investment.name,
                            amount = amountStr,
                            selectedType = investment.type,
                            selectedCurrency = investment.currency,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = UiText.DynamicString(e.message ?: "Error loading investment")) }
            }
        }
    }

    private fun deleteInvestment() {
        _state.update { it.copy(showDeleteDialog = false) }
        val id = _state.value.investmentId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val investment = repository.getInvestmentById(id).firstOrNull()
                if (investment != null) {
                    repository.deleteInvestment(investment)
                } else {
                    repository.deleteInvestment(
                        Investment(
                            id = id,
                            name = _state.value.name,
                            type = _state.value.selectedType,
                            initialAmount = _state.value.amount.toDoubleOrNull() ?: 0.0,
                            currency = _state.value.selectedCurrency,
                            dateCreated = existingDateCreated ?: System.currentTimeMillis()
                        )
                    )
                }
                _events.send(AddInvestmentEvent.InvestmentDeleted)
            } catch (e: Exception) {
                _events.send(AddInvestmentEvent.ShowError(UiText.DynamicString(e.message ?: "Error deleting investment")))
            } finally {
                _state.update { it.copy(isLoading = false) }
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
                    id = currentState.investmentId ?: 0L,
                    name = currentState.name,
                    type = currentState.selectedType,
                    initialAmount = currentState.amount.toDoubleOrNull() ?: 0.0,
                    currency = currentState.selectedCurrency,
                    dateCreated = existingDateCreated ?: System.currentTimeMillis()
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
