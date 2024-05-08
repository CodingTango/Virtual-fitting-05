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
            LoginScreen (
                onNextButtonClicked = {navController.navigate("searchScreen")}
            )
        }
        composable(route = "searchScreen"){
            SearchScreen (
                onBackButtonClicked = {navController.navigate("login")},
                onImageButton1Clicked = {navController.navigate("productScreen")}
            )
        }
        composable(route = "productScreen"){
            ProductScreen (
                onFittingButtonClicked = {navController.navigate("fittingScreen")},
                onBackButtonClicked = {navController.navigate("searchScreen")}
            )
        }
        composable(route = "fittingScreen"){
            FittingScreen(
                onBackButtonClicked = {navController.navigate("productScreen")}
            )
        }
    }
}