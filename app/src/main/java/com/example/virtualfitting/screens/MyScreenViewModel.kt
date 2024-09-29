package com.example.virtualfitting.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualfitting.data.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class MyScreenViewModel(private val imageRepository: ImageRepository) : ViewModel() {

    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap = _imageBitmap.asStateFlow()

    fun loadLatestImage() {
        viewModelScope.launch {
            try {
                val latestImageEntity = imageRepository.getLatestImage()
                latestImageEntity?.let {
                    loadImage(it.id)
                } ?: run {
                    _imageBitmap.value = null
                    Log.e("MyScreenViewModel", "No images found")
                }
            } catch (e: Exception) {
                _imageBitmap.value = null
                Log.e("MyScreenViewModel", "Error loading latest image", e)
            }
        }
    }

    private fun loadImage(id: Int) {
        viewModelScope.launch {
            try {
                imageRepository.getImageStream(id).collect { imageEntity ->
                    imageEntity?.let {
                        val imagePath = imageEntity.imagePath
                        val file = File(imagePath)
                        if (file.exists()) {
                            val bitmap = BitmapFactory.decodeFile(imagePath)
                            _imageBitmap.value = bitmap
                        } else {
                            _imageBitmap.value = null
                            Log.e("MyScreenViewModel", "File does not exist: $imagePath")
                        }
                    }
                }
            } catch (e: Exception) {
                _imageBitmap.value = null
                Log.e("MyScreenViewModel", "Error loading image", e)
            }
        }
    }
}
