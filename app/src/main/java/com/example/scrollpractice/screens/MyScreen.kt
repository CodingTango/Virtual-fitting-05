package com.example.scrollpractice.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyScreen( onBackButtonClicked:()->Unit) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.getParcelable<Bitmap>("image")
            imageBitmap = bitmap
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "마이페이지", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(16.dp))
        imageBitmap?.let {
            Image(bitmap = it.asImageBitmap(),
                contentDescription = "User Picture",
                modifier = Modifier.size(300.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            launcher.launch(Intent(context, CameraActivity::class.java))
        }) {
            Text(text = "카메라로 사진등록")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "앨범에서 사진등록")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onBackButtonClicked() }) {
            Text(text = "Back")
        }
    }
}