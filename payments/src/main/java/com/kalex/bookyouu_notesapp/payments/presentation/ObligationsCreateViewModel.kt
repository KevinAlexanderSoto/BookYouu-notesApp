package com.kalex.bookyouu_notesapp.payments.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.usecase.AddObligationUseCase
import com.kalex.bookyouu_notesapp.payments.domain.usecase.GetObligationByIdUseCase
import com.kalex.bookyouu_notesapp.notification.AlarmScheduler
import com.kalex.bookyouu_notesapp.notification.AlarmItem
import com.kalex.bookyouu_notesapp.payments.R as PaymentsR
import android.content.Context
import com.kalex.bookyouu_notesapp.notification.AlarmFrequency
import com.kalex.bookyouu_notesapp.notification.AlarmUtils
import java.time.LocalDateTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ObligationsCreateViewModel(
    private val addObligationUseCase: AddObligationUseCase,
    private val getObligationByIdUseCase: GetObligationByIdUseCase,
    private val alarmScheduler: AlarmScheduler,
    private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ObligationsCreateState())
    val state = _state.asStateFlow()

    private val _events = Channel<ObligationsCreateEvent>()
    val events = _events.receiveAsFlow()

    init {
        val obligationId = savedStateHandle.get<Int>("obligationId")
        if (obligationId != null && obligationId != -1) {
            loadObligationForEditing(obligationId)
        }
    }

    private fun loadObligationForEditing(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, editingObligationId = id) }
            val obligation = getObligationByIdUseCase(id)
            if (obligation != null) {
                _state.update { it.copy(
                    name = obligation.name,
                    amount = String.format("%.0f", obligation.amount),
                    frequency = obligation.frequency,
                    dayOfMonth = obligation.dayOfMonth,
                    category = Category.fromName(obligation.category),
                    isReminderEnabled = obligation.reminderEnabled,
                    isLoading = false
                ) }
                validate()
            } else {
                _state.update { it.copy(isLoading = false, editingObligationId = null) }
            }
        }
    }

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
            is ObligationsCreateAction.OnReminderToggle -> {
                _state.update { it.copy(isReminderEnabled = action.enabled) }
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
        val isAmountValid = currentState.amount.replace(",", "").replace(".", "").toDoubleOrNull() != null
        val isCategoryValid = currentState.category != null
        
        _state.update { it.copy(isSaveEnabled = isNameValid && isAmountValid && isCategoryValid) }
    }

    private fun saveObligation() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val currentState = _state.value
            val obligationToSave = Obligation(
                id = currentState.editingObligationId ?: 0,
                name = currentState.name,
                amount = currentState.amount.replace(",", "").replace(".", "").toDoubleOrNull() ?: 0.0,
                dayOfMonth = currentState.dayOfMonth,
                category = currentState.category?.name() ?: "",
                frequency = currentState.frequency,
                reminderEnabled = currentState.isReminderEnabled
            )
            
            try {
                val id = addObligationUseCase(obligationToSave)
                
                if (currentState.isReminderEnabled) {
                    // Schedule notification
                    val alarmTime = AlarmUtils.calculateNextMonthlyAlarmTime(currentState.dayOfMonth)
                    alarmScheduler.schedule(
                        AlarmItem(
                            alarmId = id,
                            time = alarmTime,
                            message = context.getString(PaymentsR.string.notification_obligation_message, obligationToSave.name),
                            title = context.getString(PaymentsR.string.notification_obligation_title),
                            frequency = AlarmFrequency.MONTHLY
                        )
                    )
                } else {
                    // Cancel any existing notification for this ID
                    alarmScheduler.cancel(
                        AlarmItem(
                            alarmId = id,
                            time = LocalDateTime.now(),
                            message = "",
                            title = ""
                        )
                    )
                }
                
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
