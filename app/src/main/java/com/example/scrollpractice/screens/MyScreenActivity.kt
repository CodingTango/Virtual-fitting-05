package com.example.scrollpractice.screens

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollpractice.R

class MyScreenActivity : ComponentActivity() {
    private lateinit var imageLauncher: ActivityResultLauncher<Intent>
    private val imageBitmap = mutableStateOf<Bitmap?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ActivityResultLauncher
        imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val userImage = result.data?.extras?.getParcelable<Bitmap>("image")  // 'image' 키 사용
                imageBitmap.value = userImage
            }
        }

        setContent {
            MyScreen(imageBitmap)
        }
    }

    @Composable
    fun MyScreen(imageBitmapState: MutableState<Bitmap?>) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "마이페이지", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(16.dp))
            imageBitmapState.value?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = "User Picture", modifier = Modifier.size(300.dp))
            } ?: Image(painter = painterResource(R.drawable.man), contentDescription = "Default Image", modifier = Modifier.size(300.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val cameraIntent = Intent(this@MyScreenActivity, CameraActivity::class.java)
                imageLauncher.launch(cameraIntent)
            }) {
                Text(text = "카메라로 사진등록")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                finish() // Close the screen
            }) {
                Text(text = "Back")
            }
        }
    }
}
