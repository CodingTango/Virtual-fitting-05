package com.example.scrollpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollpractice.screens.FittingScreen
import com.example.scrollpractice.screens.LoginScreen
import com.example.scrollpractice.screens.MyScreen
import com.example.scrollpractice.screens.ProductScreen
import com.example.scrollpractice.screens.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login"){
            LoginScreen {
                navController.navigate("searchscreen")
            }
        }
        composable(route = "searchscreen"){
            SearchScreen {
                navController.navigate("myscreen")
            }
        }
        composable(route = "productscreen"){
            ProductScreen {
                navController.navigate("fittingscreen")
            }
        }
        composable(route = "myscreen"){
            MyScreen()
        }
        composable(route = "fittingscreen"){
            FittingScreen()
        }
    }
}