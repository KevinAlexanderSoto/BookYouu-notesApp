package com.kalex.bookyouu_notesapp.moreMenu

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kalex.bookyouu_notesapp.authentication.BiometricSupportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SwitchMenuViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val biometricSupport: BiometricSupportUseCase,
) : ViewModel() {

    fun authenticationSwitchState(isActive: Boolean) {
        if (isActive) {
            if (biometricSupport.checkBiometricSupport()) {
                context.getSharedPreferences(
                    "AUTHENTICATION_PREFERENCES_FLAG",
                    Context.MODE_PRIVATE,
                )
                    .edit()
                    .putString(
                        "AUTHENTICATION_PREFERENCES_STRING",
                        "true",
                    )
                    .apply()
            } else {
               //TODO
            }

        } else {
            context.getSharedPreferences(
                "AUTHENTICATION_PREFERENCES_FLAG",
                Context.MODE_PRIVATE,
            )
                .edit()
                .putString(
                    "AUTHENTICATION_PREFERENCES_STRING",
                    "false",
                )
                .apply()
        }
    }

    fun notificationSwitchState(isActive: Boolean) {

    }
}