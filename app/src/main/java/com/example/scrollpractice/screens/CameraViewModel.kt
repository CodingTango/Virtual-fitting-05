package com.example.scrollpractice.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollpractice.data.ImageEntity
import com.example.scrollpractice.data.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraViewModel(private val imageRepository: ImageRepository, private val context: Context) : ViewModel() {

    fun saveImage(imageData: ByteArray) {
        viewModelScope.launch {
            val filename = "image_${System.currentTimeMillis()}.jpg"
            val filePath = saveImageToFile(imageData, filename)
            if (filePath != null) {
                val imageEntity = ImageEntity(imagePath = filePath)
                imageRepository.insertImage(imageEntity)
                val bitmap = BitmapFactory.decodeFile(filePath)
                _bitmaps.value += bitmap
            } else {
                // 파일 저장 실패 시 처리
            }
        }
    }


    private fun saveImageToFile(imageData: ByteArray, filename: String): String? {
        val file = File(context.filesDir, filename)
        return try {
            val fos = FileOutputStream(file)
            fos.write(imageData)
            fos.close()
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()
}
