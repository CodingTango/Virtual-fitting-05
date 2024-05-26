package com.example.scrollpractice.data

import kotlinx.coroutines.flow.Flow

class OfflineImageRepository(private val imageDao: ImageDao) : ImageRepository {

    override fun getImageStream(id: Int): Flow<ImageEntity?> = imageDao.getImageById(id)

    override suspend fun insertImage(imageEntity: ImageEntity) = imageDao.insertImage(imageEntity)

    override suspend fun deleteImage(imageEntity: ImageEntity) = imageDao.delete(imageEntity)

    override suspend fun updateImage(imageEntity: ImageEntity) = imageDao.update(imageEntity)

    override suspend fun getLatestImage(): ImageEntity? = imageDao.getLatestImage() // 구현된 메소드
}
