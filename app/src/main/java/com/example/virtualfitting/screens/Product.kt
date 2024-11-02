package com.example.virtualfitting.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Product(onBackButtonClicked: () -> Unit) {

    val Clothlist = listOf("전체", "BEST", "NEW", "맨투맨/스웨트", "셔츠/블라우스", "후드 티셔츠", "니트웨어", "반소매 티셔츠", "긴소매 티셔츠", "기타 상의")
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp) // 상단 바 높이
                        .padding(horizontal = 16.dp)

                ) {
                    // Left-aligned text
                    IconButton(
                        onClick = { onBackButtonClicked() },
                        modifier = Modifier.align(Alignment.TopStart)
                            .padding(vertical = 22.dp, horizontal = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color(android.graphics.Color.parseColor("#81DAF5"))

                        )
                    }
                    Text(
                        text = "맨투맨/스웨터",
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )

                }
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)// 상단 바 아래에 구분선 추가
            }
        },

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                // 첫 번째 LazyRow
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(Clothlist.size) { index ->
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(35.dp)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = Clothlist[index])

                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)// 상단 바 아래에 구분선 추가

                /*
                // 두 번째 LazyRow
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(10) { index ->
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "아이템 $index")
                        }
                    }
                }
                */
                Spacer(modifier = Modifier.height(16.dp))

                // LazyVerticalGrid (2열)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(20) { index ->
                        Box(
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "그리드 아이템 $index")
                        }
                    }
                }
            }
        }
    )
}
