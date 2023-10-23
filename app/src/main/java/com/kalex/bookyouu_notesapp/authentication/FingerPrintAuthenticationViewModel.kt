package com.kalex.bookyouu_notesapp.authentication

import android.Manifest
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.core.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FingerPrintAuthenticationViewModel @Inject constructor(
    val context: Application,
) : ViewModel() {

    private var cancellationSignal: CancellationSignal? = null

    private val _authenticationResult = MutableStateFlow<ViewModelState<Boolean>>(ViewModelState.Loading(true))
    val authenticationResultState
    get() = _authenticationResult.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.Q)
    fun launchBiometric() {
        if (checkBiometricSupport()) {
            val biometricPrompt = BiometricPrompt.Builder(context)
                .apply {
                    setTitle(resolveStrings(R.string.prompt_info_title))
                    setSubtitle(resolveStrings(R.string.prompt_info_subtitle))
                    setDescription(resolveStrings(R.string.prompt_info_description))
                    setConfirmationRequired(false)
                    setNegativeButton(
                        resolveStrings(R.string.prompt_info_use_app_password),
                        context.mainExecutor,
                    ) { _, _ ->

                        // TODO
                    }
                }.build()
            biometricPrompt.authenticate(
                getCancellationSignal(),
                context.mainExecutor,
                authenticationCallback,
            )
        }else{
            _authenticationResult.update { ViewModelState.Error(Exception()) }
        }
    }

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback =
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                _authenticationResult.update { ViewModelState.Success(true) }
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                _authenticationResult.update { ViewModelState.Error(Exception()) }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
            }
        }

    private fun checkBiometricSupport(): Boolean {
        val keyGuardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyGuardManager.isDeviceSecure) {
            return true
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    private fun resolveStrings(
        @StringRes stringResource: Int,
    ) = context.resources.getString(stringResource)

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            _authenticationResult.update { ViewModelState.Error(Exception()) }
        }

        return cancellationSignal as CancellationSignal
    }
}
