package com.kalex.bookyouu_notesapp.permission

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun RequireCameraPermission(
    permission: String,
    content: @Composable () -> Unit = { },
) {
    val permissionState = rememberPermissionState(permission)
    val context = LocalContext.current
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            RationaleDialog(
                contentText = "Necesito acceso a la camara para tomar la foto",// todo: add strings resources
                titleText = "Permisos", // todo: add strings resources
                buttonText = "deacuerdo", // todo: add strings resources
                onRequestPermission = { permissionState.launchPermissionRequest() },
            )
        },
        permissionNotAvailableContent = {
            PermissionNotAvailable(
                context = context,
                contentText = "Ve a la configuracion y activa la camara",// todo: add strings resources
                titleText = "Permisos", // todo: add strings resources
                buttonText = "deacuerdo", // todo: add strings resources
            )
        },
        content = content,
    )
}
