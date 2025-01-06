package com.kalex.bookyouu_notesapp.authentication

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState

@Composable
fun AuthenticationMain(
    onNavigateToMainApplication: () -> Unit,
    fingerPrintAuthentication: FingerPrintAuthenticationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(stringResource(id = R.string.authentication_biometric_title))
        .setSubtitle(stringResource(id = R.string.authentication_biometric_subTitle))
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        .build()

    LaunchedEffect(
        key1 = Unit,
        block = {
            fingerPrintAuthentication.authenticate(context, promptInfo)
        }
    )

    FingerPrintBaseScreen(onAuthenticateButtonClick = {
        fingerPrintAuthentication.authenticate(context, promptInfo)
    })

    handleViewModelState(
        fingerPrintAuthentication.authenticationResultState.collectAsStateWithLifecycle(),
        onEmpty = { },
        onLoading = { },
        onSuccess = { onNavigateToMainApplication() },
        onError = { exception ->
                  //The native finger print shows feedback to the user.
        },
    )
}