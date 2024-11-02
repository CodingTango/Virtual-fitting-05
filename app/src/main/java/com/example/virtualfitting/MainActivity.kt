package com.example.virtualfitting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virtualfitting.screens.FittingScreen
import com.example.virtualfitting.screens.LoginScreen
import com.example.virtualfitting.screens.MenuScreen
import com.example.virtualfitting.screens.My
import com.example.virtualfitting.screens.Product
import com.example.virtualfitting.screens.ProductScreen
import com.example.virtualfitting.screens.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNextButtonClicked = { navController.navigate("searchScreen") }
            )
        }
        composable(route = "searchScreen") {
            SearchScreen(
                onBackButtonClicked = { navController.navigate("login") },
                onImageButton1Clicked = { navController.navigate("productScreen") },
                onMenuButtonClicked = { navController.navigate("menu") },
                onMyButtonClicked = { navController.navigate("my")}
            )
        }
        composable(route = "productScreen") {
            ProductScreen(
                onFittingButtonClicked = { navController.navigate("fittingScreen") },
                onBackButtonClicked = { navController.navigate("searchScreen") }
            )
        }
        composable(route = "fittingScreen") {
            FittingScreen(
                onBackButtonClicked = { navController.navigate("productScreen") }
            )
        }
        composable(route = "menu") {
            MenuScreen(
                onBackButtonClicked = { navController.navigate("searchScreen") },
                onNavigateToDetail = { navController.navigate("Product")}
            )
        }
        composable(route = "my") {
            My()
        }
        composable(route = "product"){
            Product(
                onBackButtonClicked = { navController.navigate("searchScreen")}
            )
        }
    }
}