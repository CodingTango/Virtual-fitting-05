package com.example.scrollpractice.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollpractice.R

@Composable
fun SearchScreen(onMyButtonClicked:()->Unit,
                 onBackButtonClicked:()->Unit,
                 onImageButton1Clicked:()->Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
                contentDescription = "cloth icon")
            //Text(text = "키뮤어 후드집업")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onMyButtonClicked() }) {
            Text(stringResource(R.string.my))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onBackButtonClicked() }) {
            Text(text = "Back")
        }
    }
}