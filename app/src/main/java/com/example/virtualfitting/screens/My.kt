package com.example.virtualfitting.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.virtualfitting.R

@Composable
fun My() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text("Menu Screen")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp) // 상단 바 높이
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
        }
        Image(
            painterResource(R.drawable.image6),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value="", onValueChange={ /* 입력값 처리 */ },
            label={ Text("Email") },
            keyboardOptions= KeyboardOptions.Default.copy(keyboardType= KeyboardType.Email))

        Spacer(modifier=Modifier.height(8.dp))

        TextField(value="", onValueChange={ /* 입력값 처리 */ },
            label={ Text("Password") },
            visualTransformation= PasswordVisualTransformation(),
            keyboardOptions= KeyboardOptions.Default.copy(keyboardType=KeyboardType.Password))

        Spacer(modifier=Modifier.height(16.dp))

        Button(onClick={ /* 버튼 클릭 처리 */ }) {
            Text("Login")
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(R.drawable.image6),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value="", onValueChange={ /* 입력값 처리 */ },
            label={ Text("Email") },
            keyboardOptions= KeyboardOptions.Default.copy(keyboardType= KeyboardType.Email))

        Spacer(modifier=Modifier.height(8.dp))

        TextField(value="", onValueChange={ /* 입력값 처리 */ },
            label={ Text("Password") },
            visualTransformation= PasswordVisualTransformation(),
            keyboardOptions= KeyboardOptions.Default.copy(keyboardType=KeyboardType.Password))

        Spacer(modifier=Modifier.height(16.dp))

        Button(onClick={ /* 버튼 클릭 처리 */ }) {
            Text("Login")
        }
    }
}
/*
@Preview(showBackground=true)
@Composable
fun PreviewLoginScreen() {
    AppTheme {
        LoginScreen()
    }
}
*/