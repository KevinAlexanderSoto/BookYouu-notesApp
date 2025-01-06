package com.kalex.bookyouu_notesapp.moreMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.authentication.BiometricSupportState
import com.kalex.bookyouu_notesapp.authentication.BiometricSupportUseCase
import com.kalex.bookyouu_notesapp.notification.AlarmScheduler
import com.kalex.bookyouu_notesapp.notification.NotificationConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwitchMenuViewModel @Inject constructor(
    private val biometricSupport: BiometricSupportUseCase,
    private val moreMenuFlagsUseCase: MoreMenuFlagsUseCase,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {
    var hasBiometricSupport = false

    fun checkBiometricSupport() {
        viewModelScope.launch {
            biometricSupport.checkBiometricSupport().collectLatest { result ->
                hasBiometricSupport = when (result) {
                    is BiometricSupportState.Error -> false
                    is BiometricSupportState.Success -> true
                }
            }
        }
    }

    fun authenticationSwitchState(isActive: Boolean) {
        if (isActive) {
            moreMenuFlagsUseCase.activateBiometricFlag()
        } else {
            moreMenuFlagsUseCase.deactivateBiometricFlag()
        }
    }

    fun notificationSwitchState(isActive: Boolean) {
        if (isActive) {
            moreMenuFlagsUseCase.activateNotificationFlag()
            alarmScheduler.schedule(NotificationConstants.alarmItem)
        } else {
            moreMenuFlagsUseCase.deactivateNotificationFlag()
            alarmScheduler.cancel(NotificationConstants.alarmItem)
        }
    }
}