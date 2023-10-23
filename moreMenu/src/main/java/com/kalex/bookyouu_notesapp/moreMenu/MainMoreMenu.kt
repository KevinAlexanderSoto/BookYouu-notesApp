package com.kalex.bookyouu_notesapp.moreMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.core.common.composables.SwitchCard
import com.kalex.bookyouu_notesapp.core.common.getAuthenticationFlag
import com.kalex.bookyouu_notesapp.core.common.getNotificationFlag

@Composable
fun MainMoreMenu(
    onBiometricAction: (Boolean) -> Unit,
    onNotificationAction: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configuracion",
            fontSize = 24.sp,
        )
        SwitchCard(
            initialState = context.getAuthenticationFlag()?.toBooleanStrictOrNull() ?: false,
            switchText = "Activar seguridad biometrica",
            onChecked = {
                onBiometricAction(it)
            }
        )
        SwitchCard(
            initialState = !context.getNotificationFlag().isNullOrEmpty(),
            switchText = "Activar notificaciones",
            onChecked = {
                onNotificationAction(it)
            }
        )
    }
}