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
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
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
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(
    imageId: String,
    onFittingButtonClicked: (String) -> Unit,
    onBackButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onMenuButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val product = remember { loadProductById(context, imageId) }
    val recommendedImages = remember { loadRecommendedImages(context, imageId) }
    //val longImages = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5)
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text(product?.brand ?: "", fontSize = 18.sp) },
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
                            if (product != null) {
                                sendTrigger(product.imagePath)
                            }
                            onFittingButtonClicked(imageId)
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
                // 메인 이미지와 제품 정보
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
                        } else {
                            println("Main image resource ID not found for ${it.imagePath}")
                        }
                        /*
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
                        } else {
                            println("Main image resource ID not found for ${it.imagePath}")
                        }*/
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it.name, fontSize = 18.sp, color = Color.Black)
                        Text("₩${it.price}", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // 내용 이미지 표시
                /*item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        longImages.forEach { imageResId ->
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.78f)
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }*/

                // 추천 이미지 제목 및 리스트
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
                            println("추천 이미지 리소스 ID: $imageResId")

                            if (imageResId != 0) {
                                Image(
                                    painter = painterResource(id = imageResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(180.dp)
                                )
                            } else {
                                println("Recommended image resource ID not found.")
                            }
                        }
                    }
                }
            }
        }
    )
}

// CSV 파일에서 제품 정보를 로드하는 함수
fun loadProductById(context: Context, imageId: String): ProductData? {
    val inputStream = context.assets.open("products.csv")
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.size >= 4 && parts[0].trim() == imageId) {
                val brand = parts[1].trim()
                val name = parts[2].trim()
                val price = parts[3].trim().toIntOrNull() ?: 0
                println("Product loaded: $brand, $name, $price")
                return ProductData(imageId, brand, name, price)
            }
        }
    }
    println("Product with ID $imageId not found")
    return null
}

// CSV 파일에서 추천 이미지를 로드하는 함수
fun loadRecommendedImages(context: Context, imageId: String): List<Int> {
    val inputStream = context.assets.open("products.csv")
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.isNotEmpty() && parts[0].trim() == imageId) {
                val recommendedImageIds = parts.drop(4).take(3)
                val resourceIds = recommendedImageIds.mapNotNull { imgId ->
                    val resId = context.resources.getIdentifier(imgId.removeSuffix(".jpg"), "drawable", context.packageName)
                    if (resId != 0) {
                        println("Recommended image loaded: $imgId -> Resource ID: $resId")
                        resId
                    } else {
                        println("Resource ID for $imgId not found")
                        null
                    }
                }
                return resourceIds
            }
        }
    }
    println("No recommended images found for ID $imageId")
    return emptyList()
}

// 클라우드로 이미지를 전송하는 함수
suspend fun sendTriggerToCloud(imageId: String) {
    withContext(Dispatchers.IO) {
        val url = URL("https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/change-test")
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            OutputStreamWriter(connection.outputStream).use { it.write("""{"imageId": "$imageId"}""") }
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                println("Failed to send Image ID")
            }
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

// CSV에서 불러온 제품 정보를 저장하는 데이터 클래스
data class ProductData(
    val imageId: String,
    val brand: String,
    val name: String,
    val price: Int,
    val imagePath: String = "$imageId.jpg"
)
