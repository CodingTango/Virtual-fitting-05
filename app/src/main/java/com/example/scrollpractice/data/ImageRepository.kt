package com.example.scrollpractice.data

import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getImageStream(id: Int): Flow<ImageEntity?>

    /**
     * Insert item in the data source
     */
    suspend fun insertImage(imageEntity: ImageEntity)

    /**
     * Delete item from the data source
     */
    suspend fun deleteImage(imageEntity: ImageEntity)

    /**
     * Update item in the data source
     */
    suspend fun updateImage(imageEntity: ImageEntity)
}