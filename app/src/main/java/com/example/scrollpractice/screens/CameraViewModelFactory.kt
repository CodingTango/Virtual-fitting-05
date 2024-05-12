package com.example.scrollpractice.screens

import CameraViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scrollpractice.data.ImageEntity
import com.example.scrollpractice.data.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentHashMap

class CameraViewModelFactory(
    private val imageRepository: ImageRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CameraViewModel(imageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ImageRepositoryImpl2 : ImageRepository {
    private val images = ConcurrentHashMap<Int, ByteArray>()
    private var nextId = 1  // To simulate auto-incrementing IDs

    override fun getImageStream(id: Int): Flow<ImageEntity?> = flow {
        emit(images[id]?.let { ImageEntity(id, it) })
    }

    override suspend fun insertImage(imageEntity: ImageEntity) {
        val id = nextId++
        images[id] = imageEntity.imageData
        println("Inserted image with ID $id")
    }

    override suspend fun deleteImage(imageEntity: ImageEntity) {
        images.remove(imageEntity.id)
    }

    override suspend fun updateImage(imageEntity: ImageEntity) {
        images[imageEntity.id] = imageEntity.imageData
    }
}