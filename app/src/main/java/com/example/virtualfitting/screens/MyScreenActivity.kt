package com.example.virtualfitting.screens

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.virtualfitting.ui.theme.ScrollPracticeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MyScreenActivity : ComponentActivity() {

    private lateinit var viewModel: MyScreenViewModel
    private val client = OkHttpClient()

    // Cloud Function URL 설정
    private val cloudFunctionUrl = "https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/upload-user-image"

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.loadLatestImage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModel 초기화
        viewModel = ViewModelProvider(this, AppViewModelProvider.Factory)[MyScreenViewModel::class.java]

        setContent {
            ScrollPracticeTheme {
                MyScreen(viewModel)
            }
        }
    }

    @Composable
    fun MyScreen(viewModel: MyScreenViewModel) {
        val context = LocalContext.current
        val imageBitmap by viewModel.imageBitmap.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadLatestImage()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DisplayImage(viewModel)

            Button(onClick = {
                startForResult.launch(Intent(context, CameraActivity::class.java))
            }) {
                Text("Open Camera")
            }

            if (imageBitmap != null) {
                Button(onClick = {
                    imageBitmap?.let {
                        uploadImage(it)
                    }
                }) {
                    Text("Upload Image")
                }
            }

            Button(onClick = {
                finish()
            }) {
                Text("Back")
            }
        }
    }

    @Composable
    fun DisplayImage(viewModel: MyScreenViewModel) {
        val imageBitmap by viewModel.imageBitmap.collectAsState()

        if (imageBitmap != null) {
            androidx.compose.foundation.Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = "User Picture",
                modifier = Modifier.size(300.dp)
            )
        } else {
            Text("No image selected")
        }
    }

    // Cloud Function을 통해 이미지 업로드 기능
    private fun uploadImage(bitmap: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Bitmap을 JPEG 형식의 임시 파일로 변환
                val tempFile = File.createTempFile("upload", ".jpg")
                val outputStream = FileOutputStream(tempFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // Cloud Function URL에 요청할 MultipartBody 생성
                val requestBody = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", tempFile.name, requestBody)
                    .build()

                // HTTP 요청 생성
                val request = Request.Builder()
                    .url(cloudFunctionUrl)
                    .post(multipartBody)
                    .build()

                // 요청 실행 및 응답 처리
                val response = client.newCall(request).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MyScreenActivity, "사진 업로드 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MyScreenActivity, "사진 업로드 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MyScreenActivity, "에러 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
