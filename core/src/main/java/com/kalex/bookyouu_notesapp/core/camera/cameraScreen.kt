package com.kalex.bookyouu_notesapp.core.camera

import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.core.R
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@Composable
fun CameraScreen(
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (Uri?) -> Unit = { },
) {
    var loadingState by remember {
        mutableStateOf(false)
    }
    var showCameraButton by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
    val appName by remember { mutableStateOf(context.resources.getString(R.string.app_name)) }

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
    if (loadingState) {
        BYLoadingIndicator()
    } else {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
        ) {
            CameraPreview(
                onUseCase = {
                    previewUseCase = it
                },
                onCameraReady = { showCameraButton = true },
            )
            AnimatedVisibility(
                visible = showCameraButton,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
                    .wrapContentSize(),
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                        .wrapContentSize(),
                    onClick = {
                        loadingState = true
                        coroutineScope.launch(Dispatchers.Default) {
                            imageCaptureUseCase.takePicture(
                                context.executor,
                                appName = appName,
                                context.contentResolver,
                            ).let { fileCaptured ->
                                loadingState = false
                                onImageFile(fileCaptured)
                            }
                        }
                    },
                    content = {
                        Icon(
                            painterResource(id = R.drawable.camera_lens_svgrepo_com),
                            contentDescription = "Take picture",
                            modifier = Modifier
                                .size(74.dp),
                        )
                    },
                )
            }
        }
    }
}
