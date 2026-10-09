package com.kalex.bookyouu_notesapp.camera.presentation

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import com.kalex.bookyouu_notesapp.camera.domain.model.ScannedReceiptData
import com.kalex.bookyouu_notesapp.core.common.UiText

data class CameraScannerState(
    val isScanning: Boolean = false,
    val isFlashEnabled: Boolean = false,
    val errorMessage: UiText? = null
)

sealed interface CameraScannerAction {
    data class OnBitmapCaptured(val bitmap: Bitmap) : CameraScannerAction
    data class OnImageUriSelected(val uri: Uri, val contentResolver: ContentResolver) : CameraScannerAction
    object OnToggleFlash : CameraScannerAction
    object OnDismissError : CameraScannerAction
    object OnCloseClick : CameraScannerAction
}

sealed interface CameraScannerEvent {
    data class ReceiptScanned(val receiptData: ScannedReceiptData) : CameraScannerEvent
    data class ShowError(val message: UiText) : CameraScannerEvent
    object NavigateBack : CameraScannerEvent
}
