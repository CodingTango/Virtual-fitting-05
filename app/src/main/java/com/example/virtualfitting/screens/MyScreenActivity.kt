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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream

class MyScreenActivity : ComponentActivity() {

    private lateinit var viewModel: MyScreenViewModel

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.loadLatestImage()
            }
        }

    // [START storage_field_declaration]
    lateinit var storage: FirebaseStorage
    // [END storage_field_declaration]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModel 초기화
        viewModel = ViewModelProvider(this, AppViewModelProvider.Factory)[MyScreenViewModel::class.java]

        setContent {
            ScrollPracticeTheme {
                MyScreen(viewModel)
            }
        }

        // [START storage_field_initialization]
        storage = Firebase.storage
        // [END storage_field_initialization]
    }

    @Composable
    fun MyScreen(viewModel: MyScreenViewModel) {
        val context = LocalContext.current
        val imageBitmap by viewModel.imageBitmap.collectAsState() // 이미지 상태 감지

        LaunchedEffect(Unit) {
            viewModel.loadLatestImage() // 최신 이미지를 로드
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

            // 이미지가 있을 때만 Upload Image 버튼을 표시
            if (imageBitmap != null) {
                Button(onClick = {
                    imageBitmap?.let {
                        uploadImage(it) // 이미지 업로드
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
            // 이미지가 있을 때만 표시
            androidx.compose.foundation.Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = "User Picture",
                modifier = Modifier.size(300.dp)
            )
        } else {
            // 이미지가 없을 때 메시지 표시
            Text("No image selected")
        }
    }

    // 이미지 업로드 기능
    fun uploadImage(bitmap: Bitmap) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val userImageRef = storageRef.child("users/user.jpg")
        // Bitmap 을 ByteArray 로 변환
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = userImageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // 업로드 실패 처리
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            // 업로드 성공 처리
            Toast.makeText(this, "사진 업로드 성공", Toast.LENGTH_SHORT).show()
        }
    }
}
