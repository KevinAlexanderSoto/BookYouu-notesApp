package com.kalex.bookyouu_notesapp

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
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.core.common.getAuthenticationFlag
import com.kalex.bookyouu_notesapp.core.common.getNotificationFlag
import com.kalex.bookyouu_notesapp.core.theme.BookYouUnotesAppTheme
import com.kalex.bookyouu_notesapp.moreMenu.MoreMenuFlagsUseCase
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.graphs.RootNavigationGraph
import com.kalex.bookyouu_notesapp.notification.AlarmScheduler
import com.kalex.bookyouu_notesapp.notification.NotificationConstants
import com.kalex.bookyouu_notesapp.permission.RequireAllPermission
import com.kalex.bookyouu_notesapp.permission.RequireNotificationPermission
import org.koin.android.ext.android.inject

/**
 * used the FragmentActivity to use the  androidx.biometric in order to support API level < 29
 * **/
class MainActivity : FragmentActivity() {
    val alarmScheduler: AlarmScheduler by inject()
    val moreMenuFlagsUseCase: MoreMenuFlagsUseCase by inject()

    @OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var startDestination = ""
        val authenticationFlag = applicationContext.getAuthenticationFlag()
        startDestination =
            if (authenticationFlag?.toBooleanStrictOrNull() == true) Route.AUTHENTICATION_ROUTE else Route.PAYMENTS
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            BookYouUnotesAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                RequireAllPermission(
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
                                    moreMenuFlagsUseCase.activateNotificationFlag()
                                }
                            })
                            RootNavigationGraph(
                                navController,
                                startDestination = startDestination,
                            )
                        },
                    )
            }
        }
    }
}
