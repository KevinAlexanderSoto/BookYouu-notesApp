package com.kalex.bookyouu_notesapp.camera.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.camera.R
import com.kalex.bookyouu_notesapp.camera.domain.usecase.ScanReceiptUseCase
import com.kalex.bookyouu_notesapp.core.common.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraScannerViewModel(
    private val scanReceiptUseCase: ScanReceiptUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CameraScannerState())
    val state = _state.asStateFlow()

    private val _events = Channel<CameraScannerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: CameraScannerAction) {
        when (action) {
            is CameraScannerAction.OnBitmapCaptured -> processBitmap(action.bitmap)
            is CameraScannerAction.OnImageUriSelected -> processUri(action)
            CameraScannerAction.OnToggleFlash -> {
                _state.update { it.copy(isFlashEnabled = !it.isFlashEnabled) }
            }
            CameraScannerAction.OnDismissError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            CameraScannerAction.OnCloseClick -> {
                viewModelScope.launch {
                    _events.send(CameraScannerEvent.NavigateBack)
                }
            }
        }
    }

    private fun processBitmap(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.Default) {
            _state.update { it.copy(isScanning = true, errorMessage = null) }
            val result = scanReceiptUseCase(bitmap)
            result.onSuccess { scannedData ->
                // Check if any useful field was recognized
                if (scannedData.totalAmount == null && scannedData.merchantName == null && scannedData.date == null) {
                    val error = UiText.StringResource(R.string.camera_scanner_error_ocr)
                    _state.update { it.copy(errorMessage = error) }
                    _events.send(CameraScannerEvent.ShowError(error))
                } else {
                    _events.send(CameraScannerEvent.ReceiptScanned(scannedData))
                }
            }.onFailure {
                val error = UiText.StringResource(R.string.camera_scanner_error_ocr)
                _state.update { it.copy(errorMessage = error) }
                _events.send(CameraScannerEvent.ShowError(error))
            }
            _state.update { it.copy(isScanning = false) }
        }
    }

    private fun processUri(action: CameraScannerAction.OnImageUriSelected) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isScanning = true, errorMessage = null) }
            try {
                val source = ImageDecoder.createSource(action.contentResolver, action.uri)
                val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    decoder.isMutableRequired = true
                }
                processBitmap(bitmap)
            } catch (_: Exception) {
                val error = UiText.StringResource(R.string.camera_scanner_error_image)
                _state.update { it.copy(isScanning = false, errorMessage = error) }
                _events.send(CameraScannerEvent.ShowError(error))
            }
        }
    }
}
