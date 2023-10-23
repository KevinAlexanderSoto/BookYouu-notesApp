package com.kalex.bookyouu_notesapp.authentication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.core.common.getAuthenticationFlag
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AuthenticationMain(
    onNavigateToMainApplication: () -> Unit,
    fingerPrintAuthentication: FingerPrintAuthenticationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val authenticationFlag = context.getAuthenticationFlag()

    if (authenticationFlag?.toBooleanStrictOrNull() == true) {
        LaunchedEffect(key1 = Unit, block = { fingerPrintAuthentication.launchBiometric() })
        handleViewModelState(
            fingerPrintAuthentication.authenticationResultState.collectAsStateWithLifecycle(),
            onEmpty = { },
            onLoading = {  },
            onSuccess = { onNavigateToMainApplication() },
            onError = {
                FingerPrintBaseScreen(onAuthenticateButtonClick = {
                    fingerPrintAuthentication.launchBiometric()
                })
            },
        )
    }else{
        onNavigateToMainApplication()
    }
}