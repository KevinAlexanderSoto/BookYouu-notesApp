package com.kalex.bookyouu_notesapp.authentication

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author kevin Alexander Soto on 11/12/2023
 * **/
class BiometricSupportUseCase @Inject constructor(
    val context: Context,
) {
    fun checkBiometricSupport(): Flow<BiometricSupportState> = flow {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val biometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> emit(BiometricSupportState.Success(true))

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> emit(
                    BiometricSupportState.Error(
                        BiometricSupportState.ErrorCases.BIOMETRIC_ERROR_NO_HARDWARE
                    )
                )

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> emit(
                    BiometricSupportState.Error(
                        BiometricSupportState.ErrorCases.BIOMETRIC_ERROR_HW_UNAVAILABLE
                    )
                )

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    emit(BiometricSupportState.Error(BiometricSupportState.ErrorCases.BIOMETRIC_ERROR_NONE_ENROLLED))
                    // Prompts the user to create credentials that your app accepts.
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
                    context.startActivity(enrollIntent)
                }

                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                    emit(BiometricSupportState.Error(BiometricSupportState.ErrorCases.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED))
                }

                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                    emit(BiometricSupportState.Error(BiometricSupportState.ErrorCases.BIOMETRIC_ERROR_UNSUPPORTED))
                }

                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                    emit(BiometricSupportState.Error(BiometricSupportState.ErrorCases.BIOMETRIC_STATUS_UNKNOWN))
                }
            }
        } else {
            val keyGuardManager =
                context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (!keyGuardManager.isDeviceSecure) {
                emit(BiometricSupportState.Error(BiometricSupportState.ErrorCases.BIOMETRIC_ERROR_NONE_ENROLLED))
            } else {
                emit(BiometricSupportState.Success(true))
            }
        }
    }
}

sealed class BiometricSupportState {
    data class Success(val isSuccess: Boolean) : BiometricSupportState()
    data class Error(val error: ErrorCases) : BiometricSupportState()
    enum class ErrorCases {
        BIOMETRIC_ERROR_NO_HARDWARE,
        BIOMETRIC_ERROR_HW_UNAVAILABLE,
        BIOMETRIC_ERROR_NONE_ENROLLED,
        BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
        BIOMETRIC_ERROR_UNSUPPORTED,
        BIOMETRIC_STATUS_UNKNOWN
    }
}