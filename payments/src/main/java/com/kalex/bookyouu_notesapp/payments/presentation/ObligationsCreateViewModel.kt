package com.kalex.bookyouu_notesapp.payments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.model.ObligationCategory
import com.kalex.bookyouu_notesapp.payments.domain.model.PaymentFrequency
import com.kalex.bookyouu_notesapp.payments.domain.usecase.AddObligationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ObligationsCreateViewModel(
    private val addObligationUseCase: AddObligationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ObligationsCreateState())
    val state = _state.asStateFlow()

    private val _events = Channel<ObligationsCreateEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: ObligationsCreateAction) {
        when (action) {
            is ObligationsCreateAction.OnNameChange -> {
                _state.update { it.copy(name = action.name) }
                validate()
            }
            is ObligationsCreateAction.OnAmountChange -> {
                _state.update { it.copy(amount = action.amount) }
                validate()
            }
            is ObligationsCreateAction.OnFrequencyChange -> {
                _state.update { it.copy(frequency = action.frequency) }
            }
            is ObligationsCreateAction.OnDayChange -> {
                _state.update { it.copy(dayOfMonth = action.day) }
            }
            is ObligationsCreateAction.OnCategoryChange -> {
                _state.update { it.copy(category = action.category) }
                validate()
            }
            ObligationsCreateAction.OnSaveClick -> {
                saveObligation()
            }
            ObligationsCreateAction.OnResetClick -> {
                _state.update { ObligationsCreateState() }
            }
        }
    }

    private fun validate() {
        val currentState = _state.value
        val isNameValid = currentState.name.isNotBlank()
        val isAmountValid = currentState.amount.toDoubleOrNull() != null
        val isCategoryValid = currentState.category != null
        
        _state.update { it.copy(isSaveEnabled = isNameValid && isAmountValid && isCategoryValid) }
    }

    private fun saveObligation() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val currentState = _state.value
            val newObligation = Obligation(
                name = currentState.name,
                amount = currentState.amount.toDoubleOrNull() ?: 0.0,
                dayOfMonth = currentState.dayOfMonth,
                category = currentState.category?.name ?: "",
                frequency = currentState.frequency
            )
            
            try {
                addObligationUseCase(newObligation)
                _state.update { it.copy(isSuccess = true) }
                _events.send(ObligationsCreateEvent.ObligationSaved)
            } catch (e: Exception) {
                _events.send(ObligationsCreateEvent.ShowError(e.message ?: "Unknown error"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
