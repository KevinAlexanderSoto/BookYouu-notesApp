package com.kalex.bookyouu_notesapp.payments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.usecase.DeleteObligationUseCase
import com.kalex.bookyouu_notesapp.payments.domain.usecase.GetObligationsUseCase
import com.kalex.bookyouu_notesapp.payments.domain.usecase.TogglePaymentUseCase
import com.kalex.bookyouu_notesapp.notification.AlarmScheduler
import com.kalex.bookyouu_notesapp.notification.AlarmItem
import java.time.LocalDateTime
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ObligationsViewModel(
    private val getObligationsUseCase: GetObligationsUseCase,
    private val togglePaymentUseCase: TogglePaymentUseCase,
    private val deleteObligationUseCase: DeleteObligationUseCase,
    private val alarmScheduler: AlarmScheduler
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
        if (_uiState.value.isSelectionMode) {
            toggleSelection(item.id)
            return
        }
        viewModelScope.launch {
            togglePaymentUseCase(item)
        }
    }

    fun onDeleteObligation(id: Int) {
        viewModelScope.launch {
            deleteObligationUseCase(id)
            cancelNotification(id)
        }
    }

    fun toggleSelection(id: Int) {
        _uiState.update { state ->
            val newSelected = if (state.selectedObligations.contains(id)) {
                state.selectedObligations - id
            } else {
                state.selectedObligations + id
            }
            state.copy(
                selectedObligations = newSelected,
                isSelectionMode = newSelected.isNotEmpty()
            )
        }
    }

    fun clearSelection() {
        _uiState.update { it.copy(
            selectedObligations = emptySet(),
            isSelectionMode = false
        )}
    }

    fun deleteSelectedObligations() {
        val selectedIds = _uiState.value.selectedObligations.toList()
        viewModelScope.launch {
            deleteObligationUseCase.deleteMultiple(selectedIds)
            selectedIds.forEach { cancelNotification(it) }
            clearSelection()
        }
    }

    private fun cancelNotification(id: Int) {
        alarmScheduler.cancel(
            AlarmItem(
                alarmId = id,
                time = LocalDateTime.now(), // Time doesn't matter for cancellation
                message = "",
                title = ""
            )
        )
    }
}
