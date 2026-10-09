package com.kalex.bookyouu_notesapp.camera.presentation

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.camera.R
import com.kalex.bookyouu_notesapp.camera.domain.model.ScannedReceiptData
import com.kalex.bookyouu_notesapp.permission.RequireCameraPermission
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScannerRoot(
    viewModel: CameraScannerViewModel,
    onReceiptScanned: (ScannedReceiptData) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is CameraScannerEvent.ReceiptScanned -> {
                    onReceiptScanned(event.receiptData)
                }
                is CameraScannerEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }
                CameraScannerEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    RequireCameraPermission(
        permission = listOf(Manifest.permission.CAMERA),
        onPermissionDenied = onNavigateBack,
    ) {
        CameraScannerScreen(
            state = state,
            snackbarHostState = snackbarHostState,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
        )
    }
}

@Composable
fun CameraScannerScreen(
    state: CameraScannerState,
    snackbarHostState: SnackbarHostState,
    onAction: (CameraScannerAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor: Executor = remember(context) { ContextCompat.getMainExecutor(context) }

    var camera by remember { mutableStateOf<Camera?>(null) }
    var previewUseCase by remember { mutableStateOf<Preview?>(null) }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // Gallery Picker Launcher
    //TODO: Do we need a permission for this ?
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onAction(CameraScannerAction.OnImageUriSelected(uri, context.contentResolver))
        }
    }

    // Camera Lifecycle binding
    LaunchedEffect(previewUseCase) {
        val preview = previewUseCase ?: return@LaunchedEffect
        try {
            val cameraProvider = context.getProcessCameraProvider()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture,
            )
        } catch (ex: Exception) {
            Log.e("CameraScannerScreen", "Failed to bind camera use cases", ex)
        }
    }

    // Flash / Torch toggle observation
    LaunchedEffect(state.isFlashEnabled, camera) {
        try {
            camera?.cameraControl?.enableTorch(state.isFlashEnabled)
        } catch (e: Exception) {
            Log.e("CameraScannerScreen", "Failed to toggle torch", e)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        // 1. CameraX Preview Layer
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                }.also { previewView ->
                    previewUseCase = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                }
            },
        )

        // 2. Viewfinder Target Frame with Cutout Scrim
        ReceiptViewfinderOverlay(
            isScanning = state.isScanning,
        )

        // 3. Top Action Bar (Close & Flash toggle)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 40.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                )
            }

            IconButton(
                onClick = { onAction(CameraScannerAction.OnToggleFlash) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
            ) {
                Icon(
                    imageVector = if (state.isFlashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = if (state.isFlashEnabled) stringResource(R.string.camera_scanner_flash_off) else stringResource(R.string.camera_scanner_flash_on),
                    tint = if (state.isFlashEnabled) Color(0xFFFFD700) else Color.White,
                )
            }
        }

        // 4. Hint Text & Status Message
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.65f),
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                Text(
                    text = if (state.isScanning) {
                        stringResource(R.string.camera_scanner_processing)
                    } else {
                        stringResource(R.string.camera_scanner_hint)
                    },
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }

        // 5. Bottom Controls Bar (Gallery & Shutter)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Gallery Picker
            IconButton(
                onClick = { galleryLauncher.launch("image/*") },
                enabled = !state.isScanning,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = stringResource(R.string.camera_scanner_pick_gallery),
                    tint = Color.White,
                    modifier = Modifier.size(26.dp),
                )
            }

            // Shutter Button
            FloatingActionButton(
                onClick = {
                    if (!state.isScanning) {
                        takePicture(imageCapture, mainExecutor) { bitmap ->
                            if (bitmap != null) {
                                onAction(CameraScannerAction.OnBitmapCaptured(bitmap))
                            }
                        }
                    }
                },
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                containerColor = Color(0xFF004D40), // Dark Green Aesthetic
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
            ) {
                if (state.isScanning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = Color.White,
                        strokeWidth = 3.dp,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .border(3.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                        )
                    }
                }
            }

            // Balancing placeholder for layout symmetry
            Spacer(modifier = Modifier.size(52.dp))
        }

        // 6. Snackbar Host for error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 90.dp),
        )
    }
}

/**
 * Viewfinder overlay drawing a transparent window over the physical receipt area
 * with dark green corners and an animated laser bar when active.
 */
@Composable
fun ReceiptViewfinderOverlay(
    isScanning: Boolean,
) {
    val laserTransition = rememberInfiniteTransition(label = "laser_transition")
    val laserProgress by laserTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "laser_progress",
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        // Define receipt cutout rect (roughly 75% width, 60% height, centered)
        val rectWidth = width * 0.90f
        val rectHeight = height * 0.65f
        val left = (width - rectWidth) / 2f
        val top = (height - rectHeight) / 2.3f
        val right = left + rectWidth
        val bottom = top + rectHeight

        // Scrim canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cutoutPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(left, top, right, bottom),
                        cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
                    )
                )
            }

            clipPath(cutoutPath, clipOp = ClipOp.Difference) {
                drawRect(color = Color.Black.copy(alpha = 0.55f))
            }
        }

        // Target Frame Border & Corners
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = (left / width * maxWidth.value).dp,
                    top = (top / height * maxHeight.value).dp,
                    end = (left / width * maxWidth.value).dp,
                    bottom = ((height - bottom) / height * maxHeight.value).dp,
                )
                .border(2.dp, Color(0xFF004D40), RoundedCornerShape(16.dp)),
        ) {
            // Animated Scanning Laser Bar
            AnimatedVisibility(
                visible = isScanning,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .padding(top = (laserProgress * rectHeight).dp)
                        .background(Color(0xFF00E676)),
                )
            }
        }
    }
}

/**
 * Captures in-memory bitmap from CameraX ImageCapture with correct rotation applied.
 */
private fun takePicture(
    imageCapture: ImageCapture,
    executor: Executor,
    onBitmapReady: (Bitmap?) -> Unit,
) {
    imageCapture.takePicture(
        executor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val rawBitmap = imageProxy.toBitmap()
                val rotatedBitmap = if (rotationDegrees != 0) {
                    val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                    Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
                } else {
                    rawBitmap
                }
                imageProxy.close()
                onBitmapReady(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraScanner", "Image capture failed: ${exception.message}", exception)
                onBitmapReady(null)
            }
        }
    )
}

/**
 * Coroutine extension to fetch [ProcessCameraProvider].
 */
private suspend fun android.content.Context.getProcessCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { future ->
            future.addListener(
                { continuation.resume(future.get()) },
                ContextCompat.getMainExecutor(this),
            )
        }
    }
