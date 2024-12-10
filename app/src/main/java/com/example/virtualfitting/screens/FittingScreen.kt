package com.example.virtualfitting.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.RadioButton
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
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FittingScreen(imageId: String, onBackButtonClicked: () -> Unit) {
    val context = LocalContext.current
    var imageUrl by remember { mutableStateOf<String?>(null) }
    val loadingMessage by remember { mutableStateOf("이 작업은 최대 1분 소요될 수 있습니다.\nloading...") }
    var selectedPreference by remember { mutableStateOf<Int?>(null) }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun submitPreference(
        imageId: String,
        preference: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = URL("https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/user-update-vr")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    outputStream.write(
                        """{"imageId": "$imageId", "preference": $preference}""".toByteArray()
                    )
                }
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("제출 실패: $responseCode")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onError("네트워크 에러: ${e.message}")
                }
            }
        }
    }

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
            delay(5000)
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "결과에 대한 선호도를 선택하세요",
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    val preferences = listOf("매우 불만족", "불만족", "보통", "만족", "매우 만족")
                    preferences.forEachIndexed { index, preference ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(
                                selected = selectedPreference == index + 1,
                                onClick = { selectedPreference = index + 1 }
                            )
                            Text(
                                text = preference,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Check if preference is selected
                        selectedPreference?.let { preference ->
                            // Pass parameters to submitPreference
                            submitPreference(
                                imageId = imageId, // The imageId from the FittingScreen
                                preference = preference, // The selected preference
                                onSuccess = {
                                    // Handle success, e.g., show a toast and navigate back
                                    showToast("제출 성공!")
                                    onBackButtonClicked()
                                },
                                onError = { error ->
                                    // Handle error, e.g., show a toast with the error message
                                    showToast(error)
                                }
                            )
                        } ?: showToast("선호도를 선택해주세요.")
                    },
                    enabled = selectedPreference != null // Enable only if a preference is selected
                ) {
                    Text(text = "제출")
                }
                Spacer(modifier = Modifier.height(16.dp))

                // 뒤로가기 버튼
                Button(
                    onClick = { onBackButtonClicked() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("뒤로가기")
                }
            }
        }
    }
}
