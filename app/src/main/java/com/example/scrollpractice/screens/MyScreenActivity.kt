package com.example.scrollpractice.screens

// MyScreenActivity.kt
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

class MyScreenActivity : ComponentActivity() {
    private lateinit var imageLauncher: ActivityResultLauncher<Intent>
    private var imageBitmap = mutableStateOf<Bitmap?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photoPath = result.data?.getStringExtra("photo_path")
                photoPath?.let {
                    val bitmap = BitmapFactory.decodeFile(it)
                    imageBitmap.value = bitmap
                }
            }
        }

        setContent {
            MyScreen()
        }
    }

    @Composable
    fun MyScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageBitmap.value?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = "User Picture", modifier = Modifier.size(300.dp))
            } ?: Text("No image selected")
            Button(onClick = {
                val cameraIntent = Intent(this@MyScreenActivity, CameraActivity::class.java)
                imageLauncher.launch(cameraIntent)
            }) {
                Text("Open Camera")
            }
            Button(onClick = {
                finish()
            }) {
                Text("Back")
            }
        }
    }
}