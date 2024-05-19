package com.example.scrollpractice.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow


@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(imageEntity: ImageEntity)

    @Update
    suspend fun update(imageEntity: ImageEntity)

    @Delete
    suspend fun delete(imageEntity: ImageEntity)

    @Query("SELECT * FROM images WHERE id = :id")
    fun getImageById(id: Int): Flow<ImageEntity?>
}
