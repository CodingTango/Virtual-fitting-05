package com.example.virtualfitting.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.NumberFormat
import java.util.Locale


data class CsvProduct(
    val imagePath: String,
    val brand: String,
    val name: String,
    val price: Int
)

fun loadProductsFromCsv(context: Context): List<CsvProduct> {
    val products = mutableListOf<CsvProduct>()
    val inputStream = context.assets.open("products.csv")
    val reader = BufferedReader(InputStreamReader(inputStream))

    reader.useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.size == 4) {
                val imagePath = parts[0].trim()
                val brand = parts[1].trim()
                val name = parts[2].trim()
                val price = parts[3].trim().toIntOrNull() ?: 0
                products.add(CsvProduct(imagePath, brand, name, price))
            }
        }
    }
    return products
}

@Composable
fun formatPrice(price: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return "₩ ${formatter.format(price)}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Product(
    onBackButtonClicked: () -> Unit,
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onProductClicked: (String) -> Unit // 선택한 이미지 ID를 전달하도록 수정
) {
    val context = LocalContext.current
    val products = remember { loadProductsFromCsv(context) }

    val categoryList = listOf(
        "전체", "BEST", "NEW", "셔츠/블라우스", "후드 티셔츠",
        "니트웨어", "반소매 티셔츠", "긴소매 티셔츠", "기타 상의"
    )
    var selectedIcon by remember { mutableStateOf("Menu") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    IconButton(
                        onClick = { onBackButtonClicked() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(vertical = 22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.Gray
                        )
                    }
                    Text(
                        text = "맨투맨/스웨트",
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIcon = selectedIcon,
                onIconSelected = { selectedIcon = it },
                onMenuButtonClicked = onMenuButtonClicked,
                onMyButtonClicked = onMyButtonClicked,
                onHomeButtonClicked = onHomeButtonClicked
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(categoryList.size) { index ->
                        ElevatedCard(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = categoryList[index],
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(products.size) { index ->
                        val product = products[index]
                        ProductCard(
                            imagePath = product.imagePath,
                            brand = product.brand,
                            name = product.name,
                            price = formatPrice(product.price),
                            onClick = {
                                sendImageId(product.imagePath) // Cloud Function에 imageId 전송
                                onProductClicked(product.imagePath) // ProductDetail로 이동하며 ID전달
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ProductCard(imagePath: String, brand: String, name: String, price: String, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .height(230.dp)
            .fillMaxWidth()
            .clickable { onClick() }, // 클릭 이벤트 추가
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)
        ) {
            val context = LocalContext.current
            val imageId = context.resources.getIdentifier(imagePath.removeSuffix(".jpg"), "drawable", context.packageName)
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = brand,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = name,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = price,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


// suspend 함수로 네트워크 작업을 비동기로 처리
suspend fun sendImageIdToCloudFunction(imageId: String) {
    withContext(Dispatchers.IO) {  // 네트워크 작업을 IO 디스패처로 안전하게 이동
        val url = URL("https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/copy-selected-cloth")
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

// 코루틴 스코프에서 비동기 호출
fun sendImageId(imageId: String) {
    CoroutineScope(Dispatchers.IO).launch {
        sendImageIdToCloudFunction(imageId)
    }
}

