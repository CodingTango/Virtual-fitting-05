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
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.virtualfitting.R
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // 현재 선택된 하단바 아이콘을 관리하는 상태
    var selectedIcon by remember { mutableStateOf("Home") }
    val pyeongFontFamily = FontFamily(
        Font(R.font.pyeongchangpeace,FontWeight.Normal)
    )

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
                        fontSize = 30.sp,
                        fontFamily = pyeongFontFamily,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        IconButton(onClick = { /* 알림 클릭 */ }) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.Black
                            )
                        }
                        IconButton(onClick = { /* 쇼핑백 클릭 */ }) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Shopping Bag",
                                tint = Color.Black
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
                                isSelected = selectedIcon == "Menu",
                                onClick = {
                                    selectedIcon = "Menu"
                                    onMenuButtonClicked()
                                },
                                colorCode = "#ABB2B9"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Home,
                                label = "Home",
                                isSelected = selectedIcon == "Home",
                                onClick = {
                                    selectedIcon = "Home"
                                    onHomeButtonClicked()
                                },
                                colorCode = "#ABB2B9"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Favorite,
                                label = "Favorite",
                                isSelected = selectedIcon == "Favorite",
                                onClick = { selectedIcon = "Favorite" },
                                colorCode = "#ABB2B9"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Person,
                                label = "My",
                                isSelected = selectedIcon == "My",
                                onClick = {
                                    selectedIcon = "My"
                                    onMyButtonClicked()
                                },
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
                /*Text(
                    text = "진행중인 이벤트",
                    fontSize = 20.sp,
                    maxLines = 1,
                    color = Color.Black
                )*/
                Spacer(modifier = Modifier.height(16.dp))
                // Place the ImageSlider directly below the top bar
                ImageSlider()

                // Position buttons in the middle between ImageSlider and bottom bar
                Spacer(modifier = Modifier.height(60.dp))

                CustomButton(
                    onClick = { context.startActivity(Intent(context, MyScreenActivity::class.java)) },
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
    isSelected: Boolean,
    onClick: () -> Unit,
    colorCode: String
) {
    val iconTint = if (isSelected) Color.Black else Color(android.graphics.Color.parseColor(colorCode))

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
            tint = iconTint,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = iconTint
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
