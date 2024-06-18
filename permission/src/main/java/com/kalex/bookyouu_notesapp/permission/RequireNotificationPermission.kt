package com.kalex.bookyouu_notesapp.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun RequireNotificationPermission(
    permission: List<String> = listOf(
        android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
        android.Manifest.permission.SCHEDULE_EXACT_ALARM,
        android.Manifest.permission.USE_EXACT_ALARM,
        Manifest.permission.POST_NOTIFICATIONS,
    ),
    onPermissionDenied: () -> Unit,
    content: @Composable () -> Unit = { },
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permission)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
    } else {
        content()
    }
}
