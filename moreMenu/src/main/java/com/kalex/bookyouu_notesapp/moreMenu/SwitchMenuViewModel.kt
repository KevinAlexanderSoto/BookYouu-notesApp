package com.kalex.bookyouu_notesapp.moreMenu

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SwitchMenuViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    fun authenticationSwitchState(isActive: Boolean) {
        if (isActive) {
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