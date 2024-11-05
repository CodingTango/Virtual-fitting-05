package com.example.virtualfitting.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.virtualfitting.R
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color.White,
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "No.5",
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        IconButton(onClick = { /* 알림 클릭 */ }) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(android.graphics.Color.parseColor("#81DAF5"))
                            )
                        }
                        IconButton(onClick = { /* 쇼핑백 클릭 */ }) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingBag,
                                contentDescription = "Shopping Bag",
                                tint = Color(android.graphics.Color.parseColor("#81DAF5"))
                            )
                        }
                    }
                }
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                BottomAppBar(
                    modifier = Modifier.height(70.dp),
                    containerColor = Color.White,
                    content = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BottomBarItem(
                                icon = Icons.Filled.Menu,
                                label = "Menu",
                                onClick = { onMenuButtonClicked() },
                                colorCode = "#ABB2B9"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Search,
                                label = "Search",
                                onClick = { /* 검색 클릭 */ },
                                colorCode = "#ABB2B9"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Home,
                                label = "Home",
                                onClick = { /* 홈 클릭 */ },
                                colorCode = "#ABB2B9"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Favorite,
                                label = "Favorite",
                                onClick = { /* 즐겨찾기 클릭 */ },
                                colorCode = "#ABB2B9"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Person,
                                label = "My",
                                onClick = { onMyButtonClicked() },
                                colorCode = "#ABB2B9"
                            )
                        }
                    }
                )
            }
        },



        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                // Place the ImageSlider directly below the top bar
                ImageSlider()

                // Position buttons in the middle between ImageSlider and bottom bar
                Spacer(modifier = Modifier.height(60.dp)) // Adjust spacing as needed
                /*Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { onImageButton1Clicked() }) {
                        Text(text = "My")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { onBackButtonClicked() }) {
                        Text(text = "Back")
                    }
                }*/

                CustomButton(
                    onClick = {  context.startActivity(Intent(context, MyScreenActivity::class.java)) },
                    modifier = Modifier.width(250.dp) // 원하는 버튼 길이로 조정
                )
            }
        }
    )

}

@Composable
fun BottomBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    colorCode: String
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(android.graphics.Color.parseColor(colorCode)),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = Color(android.graphics.Color.parseColor(colorCode))
        )
    }
}

@Composable
fun ImageSlider() {
    val imageSlider = listOf(
        R.drawable.ad_image1,
        R.drawable.ad_image2,
        R.drawable.ad_image3,
        R.drawable.ad_image4
    )
    val pagerState = rememberPagerState(
        pageCount = { imageSlider.size }
    )
    val contentPadding = 50.dp
    val pageSpacing = 16.dp
    val scaleSizeRatio = 0.8f

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), // Adjust height as necessary
            state = pagerState,
            key = { imageSlider[it] },
            contentPadding = PaddingValues(horizontal = contentPadding),
            pageSpacing = pageSpacing,
        ) { page ->
            Image(
                painter = painterResource(id = imageSlider[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val pageOffset =
                            pagerState.currentPage - page + pagerState.currentPageOffsetFraction
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                        )
                        lerp(
                            start = 1f,
                            stop = scaleSizeRatio,
                            fraction = pageOffset.absoluteValue.coerceIn(0f, 1f),
                        ).let {
                            scaleX = it
                            scaleY = it
                            val sign = if (pageOffset > 0) 1 else -1
                            translationX = sign * size.width * (1 - it) / 2
                        }
                    },
            )
        }
    }
}

@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.width(200.dp) // 버튼 길이를 조정할 수 있습니다.
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        shape = RoundedCornerShape(16.dp), // 둥근 모서리
        modifier = modifier
    ) {
        Text(
            text = "사용자 이미지 등록하기",
            color = Color.White // 흰색 텍스트
        )
    }
}