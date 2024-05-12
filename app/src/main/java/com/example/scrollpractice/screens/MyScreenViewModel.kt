package com.example.scrollpractice.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollpractice.data.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyScreenViewModel(private val imageRepository: ImageRepository) : ViewModel() {

    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap = _imageBitmap.asStateFlow()

    fun loadImage(id: Int) {
        viewModelScope.launch {
            imageRepository.getImageStream(id).collect { imageEntity ->
                imageEntity?.let {
                    _imageBitmap.value = BitmapFactory.decodeByteArray(imageEntity.imageData, 0, imageEntity.imageData.size)
                }
            }
        }
    }
}
