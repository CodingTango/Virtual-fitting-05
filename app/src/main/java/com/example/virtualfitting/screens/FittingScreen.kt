package com.example.virtualfitting.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var loading by remember { mutableStateOf(true) } // 로딩 상태 추가
    var errorMessage by remember { mutableStateOf<String?>(null) } // 오류 메시지 추가

    // Firebase에서 이미지 불러오기
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()

        try {
            // output_test 이미지 불러오기
            val userRef = storage.reference.child("openpose/output_test/user_rendered.png")
            userImageUri = userRef.downloadUrl.await()
            loading = false // 로딩 완료
        } catch (e: Exception) {
            loading = false // 로딩 완료
            errorMessage = "이미지를 불러오는 중 오류가 발생했습니다: ${e.message}"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // 콘텐츠를 중앙에 정렬
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            loading -> {
                Text("Loading...")
                Spacer(modifier = Modifier.height(16.dp)) // 간격 추가
                Button(onClick = { onBackButtonClicked() }) {
                    Text(text = "Back")
                }
            }
            errorMessage != null -> {
                Text(errorMessage ?: "") // null인 경우 빈 문자열로 처리
                Spacer(modifier = Modifier.height(16.dp)) // 간격 추가
                Button(onClick = { onBackButtonClicked() }) {
                    Text(text = "Back")
                }
            }
            userImageUri != null -> {
                // 이미지가 로드되었을 때 출력
                Image(
                    painter = rememberAsyncImagePainter(userImageUri),
                    contentDescription = "Rendered Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f), // 이미지가 화면의 80%를 차지
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp)) // 이미지와 버튼 사이 간격
                Button(onClick = { onBackButtonClicked() }) {
                    Text(text = "뒤로가기")
                }
            }
        }
    }
}
