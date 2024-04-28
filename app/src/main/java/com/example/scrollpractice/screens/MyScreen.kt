package com.example.scrollpractice.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollpractice.R

@Composable
fun MyScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "마이페이지", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.man),
            contentDescription = "User Picture",
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            context.startActivity(Intent(context, CameraActivity::class.java))
        }) {
            Text(text = "카메라로 사진등록")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "앨범에서 사진등록")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { }) {
            Text(text = "Back")
        }
    }
}