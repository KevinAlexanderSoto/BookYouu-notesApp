package com.kalex.bookyouu_notesapp.moreMenu

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kalex.bookyouu_notesapp.authentication.BiometricSupportUseCase
import com.kalex.bookyouu_notesapp.notification.AlarmScheduler
import com.kalex.bookyouu_notesapp.notification.NotificationConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SwitchMenuViewModel @Inject constructor(
    private val biometricSupport: BiometricSupportUseCase,
    private val moreMenuFlagsUseCase: MoreMenuFlagsUseCase,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {

    fun authenticationSwitchState(isActive: Boolean) {
        if (isActive) {
            if (biometricSupport.checkBiometricSupport()) {
                moreMenuFlagsUseCase.activateBiometricFlag()
            } else {
                //TODO: not biometric support
            }
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