package com.example.virtualfitting.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(onNextButtonClicked:()->Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Virtual Fitting Service", fontSize = 50.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "다음으로 가려면 버튼을 누르세요", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            onNextButtonClicked()
        }) {
            Text(text = "Next")
        }
    }
}