package com.example.virtualfitting.screens

import androidx.compose.runtime.Composable

@Composable
fun Hoodie(
    onBackButtonClicked: () -> Unit,
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onProductClicked: (String) -> Unit,
    csvFileName: String,
    categoryList: List<String>,
    title: String
) {
    Product(
        csvFileName = csvFileName, // 전달받은 csvFileName 사용
        categoryList = categoryList, // 전달받은 categoryList 사용
        title = title, // 전달받은 title 사용
        //csvFileName = "shirt.csv",
        //categoryList = listOf("전체", "BEST", "NEW", "맨투맨", "후드", "스웨트"),
        //title = "셔츠/블라우스",
        onBackButtonClicked = onBackButtonClicked,
        onMenuButtonClicked = onMenuButtonClicked,
        onMyButtonClicked = onMyButtonClicked,
        onHomeButtonClicked = onHomeButtonClicked,
        onProductClicked = onProductClicked
    )
}