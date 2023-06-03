package com.kalex.sp_aplication.camara

import android.Manifest
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_ZERO_SHUTTER_LAG
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.camera.CameraPreview
import com.kalex.bookyouu_notesapp.permission.RequireCameraPermission
import kotlinx.coroutines.launch
import java.io.File

@ExperimentalPermissionsApi
@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalZeroShutterLag::class)
fun CameraScreen(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { },
) {
    val context = LocalContext.current
    RequireCameraPermission(
        permission = Manifest.permission.CAMERA,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val coroutineScope = rememberCoroutineScope()
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture.Builder()
                        .setTargetResolution(Size(480, 640)) // para la resolucion
                        .setCaptureMode(CAPTURE_MODE_ZERO_SHUTTER_LAG)
                        .build(),

                )
            }
            Box {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onUseCase = {
                        previewUseCase = it
                    },
                )
                Button(
                    shape = RoundedCornerShape(70.dp),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    onClick = {
                        coroutineScope.launch {
                            imageCaptureUseCase.takePicture(context.executor).let { it ->
                                onImageFile(it)
                            }
                        }
                    },
                ) {
                    Text("Capturar")
                }
            }
            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        previewUseCase,
                        imageCaptureUseCase,
                    )
                } catch (ex: Exception) {
                    Log.e("CameraCapture", "Failed to bind camera use cases", ex)
                }
            }
        }
    }
}
