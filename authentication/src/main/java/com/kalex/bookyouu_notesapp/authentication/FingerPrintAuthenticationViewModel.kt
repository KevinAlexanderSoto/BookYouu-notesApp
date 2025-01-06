package com.kalex.bookyouu_notesapp.authentication

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FingerPrintAuthenticationViewModel @Inject constructor(
    private val biometricSupport: BiometricSupportUseCase,
) : ViewModel() {

    private val _authenticationResult =
        MutableStateFlow<ViewModelState<Boolean>>(ViewModelState.Loading(true))
    val authenticationResultState
        get() = _authenticationResult.asStateFlow()

    // A function that launches the biometric prompt
    fun authenticate(context: Context, promptInfo: BiometricPrompt.PromptInfo) {

        val biometricPrompt = BiometricPrompt(context as FragmentActivity,
            ContextCompat.getMainExecutor(context),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle the error case
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON
                    _authenticationResult.update { ViewModelState.Error(Exception(BiometricError.BIOMETRIC_ERROR.name)) }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Handle the success case
                    _authenticationResult.update { ViewModelState.Success(true) }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle the failure case
                    _authenticationResult.update { ViewModelState.Error(Exception(BiometricError.BIOMETRIC_AUTHENTICATION_FAILED.name)) }
                }
            })
        viewModelScope.launch {
            biometricSupport.checkBiometricSupport().collect { state ->
                when (state) {
                    is BiometricSupportState.Error -> {
                    Log.e("BiometricPromptViewModel", "Error: ${state.error}")
                    _authenticationResult.update { ViewModelState.Error(Exception(state.error.name)) }

                    }

                    is BiometricSupportState.Success -> {
                        biometricPrompt.authenticate(promptInfo)
                    }
                }
            }
        }
    }
}

enum class BiometricError {
    NO_BIOMETRIC_SUPPORT, BIOMETRIC_AUTHENTICATION_FAILED, BIOMETRIC_ERROR
}
