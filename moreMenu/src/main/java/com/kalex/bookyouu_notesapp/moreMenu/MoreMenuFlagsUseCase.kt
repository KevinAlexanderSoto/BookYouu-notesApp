package com.kalex.bookyouu_notesapp.moreMenu

import android.content.Context
import com.kalex.bookyouu_notesapp.notification.NotificationConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author kevin Alexander Soto on 11/13/2023
 * **/
class MoreMenuFlagsUseCase @Inject constructor(
    @ApplicationContext val context: Context,
) {
    fun activateBiometricFlag(){
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
    }

    fun deactivateBiometricFlag(){
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

    fun activateNotificationFlag(){
        context.getSharedPreferences(
            NotificationConstants.SHARED_PREFERENCES_NOTIFICATION_FLAG,
            Context.MODE_PRIVATE,
        )
            .edit()
            .putString(
                NotificationConstants.SHARED_PREFERENCES_NOTIFICATION_STRING,
                "true",
            )
            .apply()
    }

    fun deactivateNotificationFlag(){
        context.getSharedPreferences(
            NotificationConstants.SHARED_PREFERENCES_NOTIFICATION_FLAG,
            Context.MODE_PRIVATE,
        )
            .edit()
            .putString(
                NotificationConstants.SHARED_PREFERENCES_NOTIFICATION_STRING,
                "false",
            )
            .apply()
    }
}