package com.example.virtualfitting.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.virtualfitting.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(
    imageId: String,
    onFittingButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onMenuButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val product = remember { loadProductById(context, imageId) }
    val recommendedImages = listOf(R.drawable.c25, R.drawable.c13, R.drawable.c21, R.drawable.c22, R.drawable.c16)
    val longImages = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5)
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text(product?.brand ?: "") },
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
                    IconButton(onClick = { /* Shopping cart action */ }) {
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
                        TextButton(onClick = { /* Buy action */ }) {
                            Text("구매하기")
                        }
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier
                                .height(20.dp)
                                .width(1.dp)
                                .padding(horizontal = 8.dp)
                        )
                        TextButton(onClick = { onFittingButtonClicked() }) {
                            Text("가상 피팅하기")
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
                    // 선택된 이미지 표시
                    product?.let {
                        val imageResId = context.resources.getIdentifier(
                            it.imagePath.removeSuffix(".jpg"), "drawable", context.packageName
                        )
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it.name, style = MaterialTheme.typography.titleMedium, color = Color.Black)
                        Text("₩${it.price}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    // 화면 너비에 맞춘 이미지 표시
                    Column(modifier = Modifier.fillMaxWidth()) {
                        longImages.forEach { imageResId ->
                            Image(
                                painter = painterResource(imageResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.78f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 추천 이미지 제목
                    Text(
                        text = "이 상품은 어때요?",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(),
                        color = Color.Black
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    // 추천 이미지 표시
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recommendedImages) { imageResId ->
                            Image(
                                painter = painterResource(imageResId),
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
    )
}


fun loadProductById(context: Context, imageId: String): CsvProduct? {
    val inputStream = context.assets.open("products.csv")
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.size == 4 && parts[0].trim() == imageId) {
                val brand = parts[1].trim()
                val name = parts[2].trim()
                val price = parts[3].trim().toIntOrNull() ?: 0
                return CsvProduct(imageId, brand, name, price)
            }
        }
    }
    return null
}