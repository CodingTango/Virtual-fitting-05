package com.example.virtualfitting.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

@Composable
fun FittingScreen(onBackButtonClicked: () -> Unit) {
    var userImageUri by remember { mutableStateOf<Uri?>(null) }
    var clothImageUri by remember { mutableStateOf<Uri?>(null) }
    var loading by remember { mutableStateOf(true) } // 로딩 상태 추가
    var errorMessage by remember { mutableStateOf<String?>(null) } // 오류 메시지 추가

    // Firebase에서 이미지 불러오기
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        try {
            // user 이미지 불러오기
            val userRef = storage.reference.child("users/user.jpg") // 경로 수정
            userImageUri = userRef.downloadUrl.await()

            // cloth1 이미지 불러오기
            val clothRef = storage.reference.child("clothes/cloth1.jpg") // 경로 수정
            clothImageUri = clothRef.downloadUrl.await()

            loading = false // 로딩 완료
        } catch (e: Exception) {
            loading = false // 로딩 완료
            errorMessage = "이미지를 불러오는 중 오류가 발생했습니다: ${e.message}"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // 로딩 및 버튼을 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Loading...")
                    Spacer(modifier = Modifier.height(16.dp)) // 간격 추가
                    Button(onClick = { onBackButtonClicked() }) {
                        Text(text = "Back")
                    }
                }
            }
            errorMessage != null -> {
                Text(errorMessage ?: "") // null인 경우 빈 문자열로 처리
                Button(onClick = { onBackButtonClicked() }) {
                    Text(text = "Back")
                }
            }
            userImageUri != null && clothImageUri != null -> {
                // Box를 사용하여 이미지와 버튼을 구성
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f), // 이미지 영역이 80% 차지
                    contentAlignment = Alignment.Center
                ) {
                    // User 이미지 (배경)
                    Image(
                        painter = rememberAsyncImagePainter(userImageUri),
                        contentDescription = "User Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop // 이미지 채우기
                    )
                    // Cloth 이미지 (앞에 배치, 절반 크기로 줄이기)
                    Image(
                        painter = rememberAsyncImagePainter(clothImageUri),
                        contentDescription = "Cloth Image",
                        modifier = Modifier.size(150.dp), // 이미지 크기를 줄임
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // 이미지와 버튼 사이의 간격
                Button(onClick = { onBackButtonClicked() }) {
                    Text(text = "Back")
                }
            }
        }
    }
}

