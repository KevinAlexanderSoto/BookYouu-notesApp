package com.kalex.bookyouu_notesapp

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.authentication.FingerPrintAuthenticationViewModel
import com.kalex.bookyouu_notesapp.authentication.FingerPrintBaseScreen
import com.kalex.bookyouu_notesapp.core.common.theme.BookYouUnotesAppTheme
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.graphs.RootNavigationGraph
import com.kalex.bookyouu_notesapp.notification.AlarmScheduler
import com.kalex.bookyouu_notesapp.notification.NotificationConstants
import com.kalex.bookyouu_notesapp.permission.RequireNotificationPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            BookYouUnotesAppTheme {
                val navController = rememberAnimatedNavController()
                val context = LocalContext.current
                val authenticationFlag = context.getAuthenticationFlag()
                val fingerPrintAuthentication: FingerPrintAuthenticationViewModel = hiltViewModel()
                if (authenticationFlag?.toBooleanStrictOrNull() == true) {
                    fingerPrintAuthentication.launchBiometric()
                    FingerPrintBaseScreen()
                } else {
                    RequireNotificationPermission(
                        onPermissionDenied = {
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                },
                            )
                        },
                        content = {
                            LaunchedEffect(key1 = Unit, block = {
                                if (context.getNotificationFlag().isNullOrEmpty()) {
                                    alarmScheduler.schedule(NotificationConstants.alarmItem)
                                }
                            })
                            RootNavigationGraph(
                                navController,
                                startDestination = Route.SUBJECT,
                            )
                        },
                    )
                }
            }
        }
    }

    private fun Context.getNotificationFlag() = this
        .getSharedPreferences(
            NotificationConstants.SHARED_PREFERENCES_NOTIFICATION_FLAG,
            Context.MODE_PRIVATE,
        )
        .getString(
            NotificationConstants.SHARED_PREFERENCES_NOTIFICATION_STRING,
            null,
        )

    private fun Context.getAuthenticationFlag() = this
        .getSharedPreferences(
            "AUTHENTICATION_PREFERENCES_FLAG",
            Context.MODE_PRIVATE,
        )
        .getString(
            "AUTHENTICATION_PREFERENCES_STRING",
            null,
        )

}
