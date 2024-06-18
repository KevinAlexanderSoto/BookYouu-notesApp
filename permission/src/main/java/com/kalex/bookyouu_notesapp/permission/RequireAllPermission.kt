package com.kalex.bookyouu_notesapp.permission

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 * @author kevin Alexander Soto on 6/17/2024
 * **/


private val REQUIRED_PERMISSIONS =
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // ANDROID 14 >
        listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            android.Manifest.permission.USE_EXACT_ALARM,
            android.Manifest.permission.POST_NOTIFICATIONS,

        )
    }
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { //API LEVEL 33 OR ANDROID 13
        listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.USE_EXACT_ALARM,
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.SCHEDULE_EXACT_ALARM,
        )
    }
     else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { //API LEVEL 31-32 OR ANDROID 12
         listOf(
             android.Manifest.permission.CAMERA,
             android.Manifest.permission.RECORD_AUDIO,
             android.Manifest.permission.SCHEDULE_EXACT_ALARM,
             android.Manifest.permission.READ_EXTERNAL_STORAGE,
         )
     }
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // ANDROID 10 -11
        listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,

        )
    }
    else { // Android 9
        listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }

@ExperimentalPermissionsApi
@Composable
fun RequireAllPermission(
    permission: List<String> = REQUIRED_PERMISSIONS,
    onPermissionDenied: () -> Unit,
    content: @Composable () -> Unit = { },
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permission)

    if (multiplePermissionsState.allPermissionsGranted) {
        content()
    } else {
        if (multiplePermissionsState.shouldShowRationale) {
            PermissionNotAvailable(
                titleText = stringResource(id = R.string.permission_notAvailable_title_text),
                contentText = stringResource(id = R.string.permission_notAvailable_content_text),
                buttonText = stringResource(id = R.string.permission_notAvailable_button_text),
                onConfirmClick = { onPermissionDenied() },
            )
        } else {
            RationaleDialog(
                contentText = stringResource(id = R.string.permission_rationale_content_text),
                titleText = stringResource(id = R.string.permission_rationale_title_text),
                buttonText = stringResource(id = R.string.permission_rationale_button_text),
                onRequestPermission = { multiplePermissionsState.launchMultiplePermissionRequest() },
            )
        }
    }
}