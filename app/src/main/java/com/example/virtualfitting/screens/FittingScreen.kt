package com.example.virtualfitting.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FittingScreen(imageId: String, onBackButtonClicked: () -> Unit) {
    val context = LocalContext.current
    var imageUrl by remember { mutableStateOf<String?>(null) }
    val loadingMessage by remember { mutableStateOf("이 작업은 최대 1분 소요될 수 있습니다.\nloading...") }

    // 권한 요청 launcher 설정
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            imageUrl?.let { saveImageToGallery(context, it) }
        } else {
            Toast.makeText(context, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 권한 확인 후 이미지 저장
    fun checkAndSaveImage(url: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 이상
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                saveImageToGallery(context, url)
            } else {
                galleryLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            saveImageToGallery(context, url)
        }
    }

    // 이미지가 업로드될 때까지 경로 확인을 반복
    LaunchedEffect(imageId) {
        while (imageUrl == null) {
            val url = URL("https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/download-result")
            withContext(Dispatchers.IO) {
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    outputStream.write("""{"imageId": "$imageId"}""".toByteArray())
                }
                try {
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val jsonResponse = connection.inputStream.bufferedReader().use { it.readText() }
                        val response = JSONObject(jsonResponse)
                        if (response.getString("status") == "success") {
                            imageUrl = response.getString("url")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    connection.disconnect()
                }
            }
            delay(5000) // 5초 후 다시 확인
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Fitting Screen") },
                navigationIcon = {
                    IconButton(onClick = onBackButtonClicked) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageUrl == null) {
                Text(
                    text = loadingMessage,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            } else {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
                Spacer(modifier = Modifier.height(16.dp)) // 이미지와 버튼 사이 간격
                Button(
                    onClick = {  },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Save in Gallery")
                }
                Button(onClick = { onBackButtonClicked() }) {
                    Text(text = "뒤로가기")
                }
            }
        }
    }
}

// 이미지 URL을 앨범에 저장하는 함수
fun saveImageToGallery(context: Context, imageUrl: String) {
    try {
        val input = URL(imageUrl).openStream()
        val bitmap = BitmapFactory.decodeStream(input) ?: run {
            Toast.makeText(context, "Failed to decode image", Toast.LENGTH_SHORT).show()
            return
        }

        val filename = "FittingImage_${System.currentTimeMillis()}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/VirtualFitting")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri != null) {
            resolver.openOutputStream(uri).use { outputStream ->
                outputStream?.let {
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                        Toast.makeText(context, "Image saved in gallery", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "Failed to create MediaStore entry", Toast.LENGTH_SHORT).show()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving image: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
