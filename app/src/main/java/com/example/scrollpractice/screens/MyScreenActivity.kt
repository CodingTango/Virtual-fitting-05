package com.example.scrollpractice.screens

// MyScreenActivity.kt
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

class MyScreenActivity : ComponentActivity() {

    private lateinit var viewModel: MyScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, AppViewModelProvider.Factory)[MyScreenViewModel::class.java]


        setContent {
            val viewModel: MyScreenViewModel = viewModel()
            viewModel.loadImage(0)
            MyScreen(viewModel)
        }
    }

    @Composable
    fun MyScreen(viewModel: MyScreenViewModel) {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DisplayImage(viewModel)
            Button(onClick = {
                context.startActivity(Intent(context, CameraActivity::class.java))
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

    @Composable
    fun DisplayImage(viewModel: MyScreenViewModel) {
        val imageBitmap by viewModel.imageBitmap.collectAsState()
        if (imageBitmap != null) {
            androidx.compose.foundation.Image(bitmap = imageBitmap!!.asImageBitmap(), contentDescription = "User Picture", modifier = Modifier.size(300.dp))
        } else {
            Text("No image selected")
        }
    }
}