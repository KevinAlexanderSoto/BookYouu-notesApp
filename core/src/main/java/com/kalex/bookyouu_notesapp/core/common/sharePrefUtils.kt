package com.kalex.bookyouu_notesapp.core.common

import android.content.Context


 fun Context.getNotificationFlag() = this
    .getSharedPreferences(
        "notification_channel",
        Context.MODE_PRIVATE,
    )
    .getString(
        "notification",
        null,
    )

 fun Context.getAuthenticationFlag() = this
    .getSharedPreferences(
        "AUTHENTICATION_PREFERENCES_FLAG",
        Context.MODE_PRIVATE,
    )
    .getString(
        "AUTHENTICATION_PREFERENCES_STRING",
        null,
    )