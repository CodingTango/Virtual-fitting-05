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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen(
    onBackButtonClicked: () -> Unit,
    onNavigateToDetail: (String) -> Unit
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 검색 창을 상단에 고정
        EmbeddedSearchBar(
            onQueryChange = { /* 검색어 변경 처리 */ },
            isSearchActive = true,
            onActiveChanged = { /* 검색 활성화 상태 변경 처리 */ },
            onSearch = { /* 검색 실행 처리 */ },
            onBackButtonClick = { onBackButtonClicked() } // 뒤로가기 버튼 클릭 시 뒤로 가기
        )

        // 다른 화면 요소들
        Spacer(modifier = Modifier.height(20.dp))
        SplitScrollScreen(onItemClick = onNavigateToDetail)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmbeddedSearchBar(
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    onSearch: ((String) -> Unit)? = null,
    onBackButtonClick: () -> Unit, // 뒤로가기 버튼 클릭 콜백 추가
    modifier: Modifier = Modifier
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val activeChanged: (Boolean) -> Unit = { active ->
        searchQuery.value = ""
        onQueryChange("")
        onActiveChanged(active)
    }


            SearchBar(
                      query = searchQuery.value,
                      onQueryChange = { query ->
                          searchQuery.value = query
                          onQueryChange(query)
                      },
                      onSearch = { onSearch?.invoke(searchQuery.value) },
                      active = isSearchActive,
                      onActiveChange = activeChanged,
                      modifier = modifier
                          .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(20.dp)),
        placeholder = { Text("Search") },
        leadingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onBackButtonClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back",
                        tint = Color(android.graphics.Color.parseColor("#E5E7E9"))
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "검색",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        tonalElevation = 0.dp
    ) {
        // 검색 제안이나 결과 표시
    }
}
@Composable
fun SplitScrollScreen( onItemClick: (String)->Unit ) {

    val Leftlist = listOf("상의", "아우터", "하의", "신발", "가방", "액세서리", "속옷/홈웨어", "기타")
    val Rightlist = listOf(
        listOf("전체", "BEST", "NEW", "맨투맨/스웨트", "셔츠/블라우스", "후드 티셔츠", "니트웨어", "반소매 티셔츠", "긴소매 티셔츠", "기타 상의"),
        listOf("전체", "BEST", "NEW", "후드 집업", "블루종", "블레이저", "패딩", "코트", "바람막이", "레더 재킷", "플리스", "점퍼", "야상", "기타 상의"),
        listOf("전체", "BEST", "NEW", "데님 팬츠", "트레이닝 팬츠", "와이드 팬츠", "슬렉스", "쇼트", "기타 하의"),
        listOf("전체", "BEST", "NEW", "맨투맨/스웨트", "셔츠/블라우스", "후드 티셔츠", "니트웨어", "반소매 티셔츠", "긴소매 티셔츠", "기타 상의"),
        listOf("전체", "BEST", "NEW", "후드 집업", "블루종", "블레이저", "패딩", "코트", "바람막이", "레더 재킷", "플리스", "점퍼", "야상", "기타 상의"),
        listOf("전체", "BEST", "NEW", "데님 팬츠", "트레이닝 팬츠", "와이드 팬츠", "슬렉스", "쇼트", "기타 하의"),
        listOf("전체", "BEST", "NEW", "후드 집업", "블루종", "블레이저", "패딩", "코트", "바람막이", "레더 재킷", "플리스", "점퍼", "야상", "기타 상의"),
        listOf("전체", "BEST", "NEW", "데님 팬츠", "트레이닝 팬츠", "와이드 팬츠", "슬렉스", "쇼트", "기타 하의")
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        // 왼쪽 LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // 화면의 반을 차지하도록 weight 설정
                .padding(end=4.dp),
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
                        .clickable{
                            selectedIndex = index
                        },
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        VerticalDivider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        // 하단 LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // 나머지 반을 차지하도록 weight 설정
                .padding(start = 4.dp ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(Rightlist[selectedIndex].size) { index ->
                val item = Rightlist[selectedIndex][index]
                Text(
                    text = Rightlist[selectedIndex][index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            if (item == "맨투맨/스웨트") {
                                onItemClick(item)
                            }
                        },
                    color = Color.Black
                )
            }
        }
    }
}