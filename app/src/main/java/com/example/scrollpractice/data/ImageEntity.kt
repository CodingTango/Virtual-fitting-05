package com.example.scrollpractice.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

@Entity(tableName = "images")
@TypeConverters(BitmapConverter::class)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var imageData: ByteArray
)

class BitmapConverter {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray = bitmap.toByteArray()

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap = byteArray.toBitmap()
}

fun Bitmap.toByteArray(): ByteArray {
    ByteArrayOutputStream().apply {
        compress(Bitmap.CompressFormat.PNG, 100, this)
        return toByteArray()
    }
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}
