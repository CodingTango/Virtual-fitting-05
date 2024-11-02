package com.example.virtualfitting.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualfitting.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackButtonClicked: () -> Unit,
    onImageButton1Clicked: () -> Unit,
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
                        .height(60.dp) // 상단 바 높이
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    // Left-aligned text

                    Text(
                        text = "No.5",
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Right-aligned icons
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
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)// 상단 바 아래에 구분선 추가
            }
        },

        bottomBar = {
            Column {
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)// 하단 바 위에 구분선 추가
                BottomAppBar(
                    modifier = Modifier.height(60.dp), // 하단 바 두께 설정
                    containerColor = Color.White, // 배경색 설정
                    content = {
                        // 중앙 정렬로 아이콘 배치
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly, // 아이콘 중앙 정렬
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BottomBarItem(
                                icon = Icons.Filled.Menu,
                                label = "Menu",
                                onClick = { onMenuButtonClicked() },
                                colorCode = "#81DAF5" // 사용자 지정 색상
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Search,
                                label = "Search",
                                onClick = { /* 검색 클릭 */ },
                                colorCode = "#81DAF5"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Home,
                                label = "Home",
                                onClick = { /* 홈 클릭 */ },
                                colorCode = "#81DAF5"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Favorite,
                                label = "Favorite",
                                onClick = { /* 즐겨찾기 클릭 */ },
                                colorCode = "#81DAF5"
                            )
                            BottomBarItem(
                                icon = Icons.Filled.Person,
                                label = "My",
                                onClick = { onMyButtonClicked() },
                                colorCode = "#81DAF5"
                            )
                        }
                    }
                )
            }
        },

        content = { innerPadding ->
            // 본문 내용 정의
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "옷을 터치하면 상세페이지로 이동합니다.", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "My를 터치하면 마이페이지로 이동합니다.", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onImageButton1Clicked() }) {
                    Image(
                        painter = painterResource(R.drawable.image6),
                        contentDescription = "cloth icon"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    context.startActivity(Intent(context, MyScreenActivity::class.java))
                }) {
                    Text(stringResource(R.string.my))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onBackButtonClicked() }) {
                    Text(text = "Back")
                }
            }
        }
    )
}

@Composable
fun BottomBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    colorCode: String // 사용자 지정 색상 코드
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick), // 클릭 가능하게 설정
        horizontalAlignment = Alignment.CenterHorizontally, // 아이콘과 텍스트를 중앙 정렬
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(android.graphics.Color.parseColor(colorCode)), // 사용자 지정 색상 코드 적용
            modifier = Modifier.size(30.dp) // 아이콘 크기 설정
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp, // 글자 크기 설정
            textAlign = TextAlign.Center,
            color = Color(android.graphics.Color.parseColor(colorCode)) // 텍스트 색상도 같은 색상 코드 적용
        )
    }
}
