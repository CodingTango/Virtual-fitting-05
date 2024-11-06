@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.virtualfitting.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaActionSound
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.virtualfitting.CameraPreview
import com.example.virtualfitting.PhotoBottomSheetContent
import com.example.virtualfitting.ui.theme.ScrollPracticeTheme
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class CameraActivity : ComponentActivity() {
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageCapture: ImageCapture
    private lateinit var viewModel: CameraViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, AppViewModelProvider.Factory)[CameraViewModel::class.java]

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, CAMERA_PERMISSIONS, 0)
        }

        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA  // Default selector
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        setContent {
            ScrollPracticeTheme {
                val scope = rememberCoroutineScope()
                val scaffoldState = rememberBottomSheetScaffoldState()
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
                        this.cameraSelector = cameraSelector
                    }
                }

                val bitmaps by viewModel.bitmaps.collectAsState()

                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = 0.dp,
                    sheetContent = {
                        PhotoBottomSheetContent(
                            bitmaps = bitmaps,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CameraPreview(
                            controller = controller,
                            modifier = Modifier.fillMaxSize()
                        )

                        IconButton(
                            onClick = {
                                // Toggle between front and back camera
                                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                }
                                controller.cameraSelector = cameraSelector
                            },
                            modifier = Modifier.offset(16.dp, 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Switch camera"
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Photo,
                                    contentDescription = "Open gallery"
                                )
                            }
                            IconButton(
                                onClick = {
                                    setResult(RESULT_OK)
                                    finish()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "finish"
                                )
                            }
                            IconButton(
                                onClick = {
                                    takePhotoAndSave()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Take photo"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun takePhotoAndSave() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture)

                imageCapture.takePicture(ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val sound = MediaActionSound()
                        sound.play(MediaActionSound.SHUTTER_CLICK)

                        val rotationDegrees = image.imageInfo.rotationDegrees
                        val bitmap = imageProxyToBitmap(image, rotationDegrees)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val imageData = stream.toByteArray()
                        viewModel.saveImage(imageData)
                        showToast(this@CameraActivity, "image captured!")
                        image.close()
                    }

                    override fun onError(exc: ImageCaptureException) {
                        Log.e("CameraActivity", "Photo capture failed: ${exc.message}", exc)
                    }
                })
            } catch (exc: Exception) {
                Log.e("CameraActivity", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun imageProxyToBitmap(image: ImageProxy, rotationDegrees: Int): Bitmap {
        val buffer = image.planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)

        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    private fun hasRequiredPermissions(): Boolean {
        return CAMERA_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}
