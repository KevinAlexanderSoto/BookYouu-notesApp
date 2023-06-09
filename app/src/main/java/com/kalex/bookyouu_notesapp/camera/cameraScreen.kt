package com.kalex.bookyouu_notesapp.camera

import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.records.presentation.RecordsViewModel
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalZeroShutterLag::class)
fun CameraScreen(
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (Uri?) -> Unit = { },
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 20.dp),
    ) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val coroutineScope = rememberCoroutineScope()
        var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
        val appName = context.resources.getString(R.string.app_name)

        val imageCaptureUseCase by remember {
            mutableStateOf(
                ImageCapture.Builder()
                    .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build(),
            )
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
                Log.e(
                    "CameraCapture",
                    "Failed to bind camera use cases",
                    ex,
                ) // TODO: Add error message
            }
        }
        CameraPreview(
            onUseCase = {
                previewUseCase = it
            },
        )

        IconButton(
            modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                coroutineScope.launch {
                    imageCaptureUseCase.takePicture(
                        context.executor,
                        appName = appName,
                        context.contentResolver,
                    ).let { fileCaptured ->
                        onImageFile(fileCaptured)
                    }
                }
            },
            content = {
                Icon(
                    painterResource(id = R.drawable.camera_lens_svgrepo_com),
                    contentDescription = "Take picture",
                    modifier = Modifier
                        .size(100.dp),
                )
            },
        )
    }
}
