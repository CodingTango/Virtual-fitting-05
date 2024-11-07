package com.example.virtualfitting.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualfitting.R

@Composable
fun My(
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit
) {
    var selectedIcon by remember { mutableStateOf("My") }
    val pyeongFontFamily = FontFamily(
        Font(R.font.pyeongchangpeace, FontWeight.Normal)
    )

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavigationBar(
                selectedIcon = selectedIcon,
                onIconSelected = { selectedIcon = it },
                onMenuButtonClicked = onMenuButtonClicked,
                onMyButtonClicked = onMyButtonClicked,
                onHomeButtonClicked = onHomeButtonClicked
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Placeholder logo
                Text(
                    text = "No.5",
                    fontSize = 40.sp,
                    fontFamily = pyeongFontFamily,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("Login to your Account", fontSize = 15.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = "", onValueChange = { /* Handle email input */ },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = "", onValueChange = { /* Handle password input */ },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle login button click */ },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Sign in", color = Color.White)
                }



                Spacer(modifier = Modifier.height(24.dp))

                Text("Don’t have an account? Sign up", fontSize = 15.sp, color = Color.Black)
            }
        }
    )
}

