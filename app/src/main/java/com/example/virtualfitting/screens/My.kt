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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.virtualfitting.R

@Composable
fun My(
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit
) {
    //val context = LocalContext.current
    var selectedIcon by remember { mutableStateOf("My") }

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
                Text("Menu Screen")
                Image(
                    painter = painterResource(R.drawable.image6),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(value = "", onValueChange = { /* Handle email input */ },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email))

                Spacer(modifier = Modifier.height(8.dp))

                TextField(value = "", onValueChange = { /* Handle password input */ },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password))

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { /* Handle login button click */ }) {
                    Text("Login")
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(
    selectedIcon: String,
    onIconSelected: (String) -> Unit,
    onMenuButtonClicked: () -> Unit,
    onMyButtonClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit
) {
    Column {
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        BottomAppBar(
            modifier = Modifier.height(70.dp),
            containerColor = Color.White,
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomBarItem(
                        icon = Icons.Filled.Menu,
                        label = "Menu",
                        isSelected = selectedIcon == "Menu",
                        onClick = {
                            onIconSelected("Menu")
                            onMenuButtonClicked()
                        },
                        colorCode = "#ABB2B9"
                    )
                    /*BottomBarItem(
                        icon = Icons.Filled.Search,
                        label = "Search",
                        isSelected = selectedIcon == "Search",
                        onClick = { onIconSelected("Search") },
                        colorCode = "#ABB2B9"
                    )*/
                    BottomBarItem(
                        icon = Icons.Filled.Home,
                        label = "Home",
                        isSelected = selectedIcon == "Home",
                        onClick = {
                            onIconSelected("Home")
                            onHomeButtonClicked()
                        },
                        colorCode = "#ABB2B9"
                    )
                    BottomBarItem(
                        icon = Icons.Filled.Favorite,
                        label = "Favorite",
                        isSelected = selectedIcon == "Favorite",
                        onClick = { onIconSelected("Favorite") },
                        colorCode = "#ABB2B9"
                    )
                    BottomBarItem(
                        icon = Icons.Filled.Person,
                        label = "My",
                        isSelected = selectedIcon == "My",
                        onClick = {
                            onIconSelected("My")
                            onMyButtonClicked()
                        },
                        colorCode = "#ABB2B9"
                    )
                }
            }
        )
    }
}