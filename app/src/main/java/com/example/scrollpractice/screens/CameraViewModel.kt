import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollpractice.data.ImageEntity
import com.example.scrollpractice.data.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(private val imageRepository: ImageRepository) : ViewModel() {

    fun saveImage(imageData: ByteArray) {
        viewModelScope.launch {
            val imageEntity = ImageEntity(imageData = imageData)
            imageRepository.insertImage(imageEntity)
        }
    }

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()
}
