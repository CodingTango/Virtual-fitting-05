package com.example.virtualfitting.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    onBackButtonClicked: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit
) {
    var selectedIcon by remember { mutableStateOf("Menu") }

    Scaffold(
        containerColor = Color.White,
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
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 검색 창을 상단에 고정
                SearchField(
                    onBackButtonClick = { onBackButtonClicked() }
                )

                Spacer(modifier = Modifier.height(20.dp))
                SplitScrollScreen(onItemClick = onNavigateToDetail)
            }
        }
    )
}

@Composable
fun SearchField(
    onBackButtonClick: () -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        IconButton(onClick = { onBackButtonClick() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "뒤로가기",
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("검색", fontSize = 16.sp) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "검색",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

@Composable
fun SplitScrollScreen(onItemClick: (String) -> Unit) {
    val Leftlist = listOf("상의", "아우터", "하의", "신발", "가방", "액세서리", "기타")
    val Rightlist = listOf(
        listOf("전체", "BEST", "NEW", "맨투맨/스웨트", "셔츠/블라우스", "후드 티셔츠", "니트웨어", "반소매 티셔츠", "긴소매 티셔츠", "기타 상의"),
        listOf("전체", "BEST", "NEW", "후드 집업", "블루종", "블레이저", "패딩", "코트", "바람막이", "레더 재킷", "플리스", "점퍼", "야상", "기타 상의"),
        listOf("전체", "BEST", "NEW", "데님 팬츠", "트레이닝 팬츠", "와이드 팬츠", "슬렉스", "쇼트", "기타 하의"),
        listOf("전체", "BEST", "NEW", "스니커즈", "로퍼", "구두", "부츠", "샌들", "기타 신발", "신발 액세서리"),
        listOf("전체", "BEST", "NEW", "백팩", "크로스 백", "숄더백", "토트백", "웨이스트백", "랩탑백", "에코,캔버스백", "기타 가방", "가방 액세서리", "야상", "기타 상의"),
        listOf("전체", "BEST", "NEW", "모자", "머플러", "주얼리", "벨트", "지갑", "시계", "기타 액세서리"),
        listOf("전체", "BEST", "NEW", "양말", "넥타이", "장갑", "가구/인테리어"),
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(Leftlist.size) { index ->
                Text(
                    text = Leftlist[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            if (index == selectedIndex) Color.LightGray else Color.Transparent
                        )
                        .clickable {
                            selectedIndex = index
                        },
                    color = Color.Black
                )
            }
        }

        VerticalDivider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(Rightlist[selectedIndex].size) { index ->
                val item = Rightlist[selectedIndex][index]
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            if (item == "맨투맨/스웨트" || item == "셔츠/블라우스" || item == "후드 티셔츠") {
                                onItemClick(item)
                            }
                        },
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedIcon: String,
    onIconSelected: (String) -> Unit,
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit
) {
    Column {
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
                            onIconSelected("Menu")
                            onMenuButtonClicked()
                        },
                        colorCode = "#ABB2B9"
                    )
                    BottomBarItem(
                        icon = Icons.Filled.Home,
                        label = "Home",
                        isSelected = selectedIcon == "Home",
                        onClick = {
                            onIconSelected("Home")
                            onHomeButtonClicked()
                        },
                        colorCode = "#ABB2B9"
                    )
                    BottomBarItem(
                        icon = Icons.Filled.Favorite,
                        label = "Favorite",
                        isSelected = selectedIcon == "Favorite",
                        onClick = { onIconSelected("Favorite") },
                        colorCode = "#ABB2B9"
                    )
                    BottomBarItem(
                        icon = Icons.Filled.Person,
                        label = "My",
                        isSelected = selectedIcon == "My",
                        onClick = {
                            onIconSelected("My")
                            onMyButtonClicked()
                        },
                        colorCode = "#ABB2B9"
                    )
                }
            }
        )
    }
}