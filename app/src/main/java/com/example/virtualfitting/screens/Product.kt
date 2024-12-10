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
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
    val price: Int,
    val recommendedImages: List<String>
)

fun loadProductsFromCsv(context: Context, fileName: String): List<CsvProduct> {
    val products = mutableListOf<CsvProduct>()
    val inputStream = context.assets.open(fileName)
    val reader = BufferedReader(InputStreamReader(inputStream))

    reader.useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.size >= 4) {
                val imagePath = parts[0].trim()
                val brand = parts[1].trim()
                val name = parts[2].trim()
                val price = parts[3].trim().toIntOrNull() ?: 0
                val recommendedImages = parts.drop(4).map { it.trim() }
                products.add(CsvProduct(imagePath, brand, name, price, recommendedImages))
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
    onProductClicked: (String) -> Unit,
    csvFileName: String,
    categoryList: List<String>,
    title: String
) {
    val context = LocalContext.current
    val products = remember { loadProductsFromCsv(context, csvFileName) }

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
                        text = title,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Divider(color = Color.Gray, thickness = 1.dp)
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
                                sendImageId(product.imagePath)
                                onProductClicked(product.imagePath)
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
            .clickable { onClick() },
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



suspend fun sendImageIdToCloudFunction(imageId: String) {
    withContext(Dispatchers.IO) {
        val url = URL("https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/user-update")
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val jsonInputString = """{"imageId": "$imageId"}"""
            println("Sending JSON data: $jsonInputString")

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

fun sendImageId(imageId: String) {
    CoroutineScope(Dispatchers.IO).launch {
        sendImageIdToCloudFunction(imageId)
    }
}