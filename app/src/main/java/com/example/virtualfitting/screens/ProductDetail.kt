package com.example.virtualfitting.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(
    imageId: String,
    csvFileName: String, // CSV 파일 이름을 동적으로 전달받음
    onFittingButtonClicked: (String) -> Unit,
    onBackButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onMenuButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val product = remember { loadProductById(context, imageId, csvFileName) }
    val recommendedImages = remember { loadRecommendedImages(context, imageId, csvFileName) }
    var newRecommendations by remember { mutableStateOf(listOf<Int>()) } // 초기 상태 정의
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        newRecommendations = fetchRecommendationsFromServer(context)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { /*Text(product?.name ?: "Product Detail")*/ },
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onHomeButtonClicked() }) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                    }
                    IconButton(onClick = { onMenuButtonClicked() }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* 장바구니 동작 */ }) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isFavorite = !isFavorite }) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                        TextButton(onClick = { /* 구매하기 동작 */ }) {
                            Text("구매하기")
                        }
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .height(20.dp)
                                .width(1.dp)
                                .padding(horizontal = 8.dp)
                        )
                        TextButton(onClick = {
                            product?.let {
                                sendTrigger(it.imagePath)
                                onFittingButtonClicked(it.imagePath)
                            }
                        }) {
                            Text("가상피팅하기")
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    product?.let {
                        val imageResId = context.resources.getIdentifier(
                            it.imagePath.removeSuffix(".jpg"), "drawable", context.packageName
                        )
                        if (imageResId != 0) {
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(top = 16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it.brand, fontSize = 18.sp)
                        Text(it.name, fontSize = 18.sp, color = Color.Black)
                        Text("₩${it.price}", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "이 상품은 어때요?",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recommendedImages) { imageResId ->
                            if (imageResId != 0) {
                                Image(
                                    painter = painterResource(id = imageResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(180.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "사용자님의 선호도를 반영한 추천상품이에요.",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(newRecommendations) { imageResId ->
                            if (imageResId != 0) {
                                Image(
                                    painter = painterResource(id = imageResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(180.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
// CSV 파일에서 제품 정보를 로드하는 함수
fun loadProductById(context: Context, imageId: String, csvFileName: String): ProductData? {
    val inputStream = context.assets.open(csvFileName)
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.size >= 4 && parts[0].trim() == imageId) {
                val brand = parts[1].trim()
                val name = parts[2].trim()
                val price = parts[3].trim().toIntOrNull() ?: 0
                return ProductData(imageId, brand, name, price)
            }
        }
    }
    return null
}
// CSV 파일에서 추천 이미지를 로드하는 함수
fun loadRecommendedImages(context: Context, imageId: String, csvFileName: String): List<Int> {
    val inputStream = context.assets.open(csvFileName)
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.isNotEmpty() && parts[0].trim() == imageId) {
                val recommendedImageIds = parts.drop(4).take(3)
                return recommendedImageIds.mapNotNull { imgId ->
                    context.resources.getIdentifier(imgId.removeSuffix(".jpg"), "drawable", context.packageName).takeIf { it != 0 }
                }
            }
        }
    }
    return emptyList()
}
// 클라우드로 이미지를 전송하는 함수
suspend fun sendTriggerToCloud(imageId: String) {
    withContext(Dispatchers.IO) {  // 네트워크 작업을 IO 디스패처로 안전하게 이동
        val url = URL("https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/change-test")
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val jsonInputString = """{"imageId": "$imageId"}"""
            println("Sending JSON data: $jsonInputString")  // JSON 데이터 로그 출력

            val outputStreamWriter = OutputStreamWriter(connection.outputStream)
            outputStreamWriter.write(jsonInputString)
            outputStreamWriter.flush()
            outputStreamWriter.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                println("Image ID sent successfully")
            } else {
                println("Failed to send Image ID: $responseCode")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
    }
}

// 비동기로 호출하는 함수
fun sendTrigger(imageId: String) {
    CoroutineScope(Dispatchers.IO).launch {
        sendTriggerToCloud(imageId)
    }
}

suspend fun fetchRecommendationsFromServer(context: Context): List<Int> {
    return withContext(Dispatchers.IO) {
        val url = URL("https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/download-user-recommend-csv") // Cloud Function URL 입력
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val jsonResponse = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(jsonResponse)
                val recommendations = jsonObject.getJSONArray("recommendations")

                (0 until recommendations.length()).mapNotNull { index ->
                    val imgId = recommendations.getString(index)
                    context.resources.getIdentifier(
                        imgId.removeSuffix(".jpg"),
                        "drawable",
                        context.packageName
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            connection.disconnect()
        }
    }
}

// CSV에서 불러온 제품 정보를 저장하는 데이터 클래스
data class ProductData(
    val imagePath: String,
    val brand: String,
    val name: String,
    val price: Int,
)