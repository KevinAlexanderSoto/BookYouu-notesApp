package com.kalex.bookyouu_notesapp.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun RequireCameraPermission(
    permission: List<String>,
    content: @Composable () -> Unit = { },
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permission)
    val context = LocalContext.current

    if (multiplePermissionsState.allPermissionsGranted) {
        content()
    } else {
        /*       RationaleDialog(
                   contentText = "Necesito acceso a la camara para tomar la foto", // todo: add strings resources
                   titleText = "Permisos", // todo: add strings resources
                   buttonText = "deacuerdo", // todo: add strings resources
                   onRequestPermission = { multiplePermissionsState.launchMultiplePermissionRequest() },
               )

           PermissionNotAvailable(
               context = context,
               contentText = "Ve a la configuracion y activa la camara", // todo: add strings resources
               titleText = "Permisos", // todo: add strings resources
               buttonText = "deacuerdo", // todo: add strings resources
           )*/

        Column {
            Text(
                getTextToShowGivenPermissions(
                    multiplePermissionsState.revokedPermissions,
                    multiplePermissionsState.shouldShowRationale,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                Text("Request permissions")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean,
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""

    val textToShow = StringBuilder().apply {
        append("The ")
    }

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", and ")
            }

            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }

            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        },
    )
    return textToShow.toString()
}
