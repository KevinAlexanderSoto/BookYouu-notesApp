package com.kalex.bookyouu_notesapp.authentication

import android.Manifest
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import javax.inject.Inject

/**
 * @author kevin Alexander Soto on 11/12/2023
 * **/
class BiometricSupportUseCase @Inject constructor(
    val context: Context,
){
    fun checkBiometricSupport(): Boolean {
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
}