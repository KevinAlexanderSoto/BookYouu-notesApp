package com.kalex.bookyouu_notesapp.payments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.usecase.GetObligationsUseCase
import com.kalex.bookyouu_notesapp.payments.domain.usecase.TogglePaymentUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ObligationsViewModel(
    private val getObligationsUseCase: GetObligationsUseCase,
    private val togglePaymentUseCase: TogglePaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ObligationsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        getObligationsUseCase()
            .onEach { summary ->
                _uiState.update { it.copy(
                    totalBalance = summary.totalBalance,
                    pendingAmount = summary.pendingAmount,
                    paidAmount = summary.paidAmount,
                    obligations = summary.obligations,
                    isLoading = false
                )}
            }
            .launchIn(viewModelScope)
    }

    fun onPaymentToggled(item: Obligation) {
        viewModelScope.launch {
            togglePaymentUseCase(item)
        }
    }
}
