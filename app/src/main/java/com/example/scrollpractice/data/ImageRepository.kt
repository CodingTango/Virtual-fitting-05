package com.example.scrollpractice.data

import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getImageStream(id: Int): Flow<ImageEntity?>

    suspend fun insertImage(imageEntity: ImageEntity)

    suspend fun deleteImage(imageEntity: ImageEntity)

    suspend fun updateImage(imageEntity: ImageEntity)

    suspend fun getLatestImage(): ImageEntity? // 추가된 메소드
}
