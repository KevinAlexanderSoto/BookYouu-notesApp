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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * used the FragmentActivity to use the  androidx.biometric in order to support API level < 29
 * **/
@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var alarmScheduler: AlarmScheduler
    @Inject
    lateinit var moreMenuFlagsUseCase: MoreMenuFlagsUseCase

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            BookYouUnotesAppTheme {
                val navController = rememberAnimatedNavController()
                val context = LocalContext.current
                var startDestination = ""
                val authenticationFlag = context.getAuthenticationFlag()
                startDestination =
                    if (authenticationFlag?.toBooleanStrictOrNull() == true) Route.AUTHENTICATION_ROUTE else Route.SUBJECT
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
