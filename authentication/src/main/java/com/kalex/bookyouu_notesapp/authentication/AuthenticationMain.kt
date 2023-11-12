package com.kalex.bookyouu_notesapp.authentication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
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
        LaunchedEffect(
            key1 = Unit,
            block = {
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("negative button")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    .build()
                fingerPrintAuthentication.authenticate(context, promptInfo)
            }
        )
        handleViewModelState(
            fingerPrintAuthentication.authenticationResultState.collectAsStateWithLifecycle(),
            onEmpty = { },
            onLoading = {  },
            onSuccess = { onNavigateToMainApplication() },
            onError = { exception ->
                FingerPrintBaseScreen(onAuthenticateButtonClick = {
                    val promptInfo = BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Biometric login for my app")
                        .setSubtitle("Log in using your biometric credential")
                        .setNegativeButtonText("negative button")
                        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                        .build()
                    fingerPrintAuthentication.authenticate(context, promptInfo)
                })
            },
        )
    }else{
        onNavigateToMainApplication()
    }
}