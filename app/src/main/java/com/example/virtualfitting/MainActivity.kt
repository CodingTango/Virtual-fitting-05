package com.example.virtualfitting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.virtualfitting.screens.FittingScreen
import com.example.virtualfitting.screens.Home
import com.example.virtualfitting.screens.Menu
import com.example.virtualfitting.screens.My
import com.example.virtualfitting.screens.Product
import com.example.virtualfitting.screens.ProductDetail

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
    NavHost(navController = navController, startDestination = "home") {

        composable(route = "home") {
            Home(
                onMenuButtonClicked = { navController.navigate("menu") },
                onMyButtonClicked = { navController.navigate("my")},
                onHomeButtonClicked = {navController.navigate("home")}
            )
        }
        composable(
            route = "productdetail/{imageId}",
            arguments = listOf(navArgument("imageId") { defaultValue = "" })
        ) { backStackEntry ->
            val imageId = backStackEntry.arguments?.getString("imageId") ?: ""
            ProductDetail(
                imageId = imageId,
                onFittingButtonClicked = {
                    navController.navigate("fittingScreen/$imageId")
                },
                onBackButtonClicked = { navController.popBackStack() },
                onHomeButtonClicked = { navController.navigate("home") },
                onMenuButtonClicked = { navController.navigate("menu") }
            )
        }
        composable(route = "fittingScreen/{imageId}") { backStackEntry ->
            val imageId = backStackEntry.arguments?.getString("imageId") ?: ""
            FittingScreen(
                imageId = imageId,
                onBackButtonClicked = { navController.popBackStack() }
            )
        }
        composable(route = "menu") {
            Menu(
                onBackButtonClicked = { navController.navigate("home") },
                onMenuButtonClicked = { navController.navigate("menu") },
                onMyButtonClicked = { navController.navigate("my")},
                onHomeButtonClicked = {navController.navigate("home")},
                onNavigateToDetail = { navController.navigate("Product")}
            )
        }
        composable(route = "my") {
            My(
                onMenuButtonClicked = { navController.navigate("menu") },
                onMyButtonClicked = { navController.navigate("my") },
                onHomeButtonClicked = { navController.navigate("home") }
            )
        }
        composable(route = "product") {
            Product(
                onBackButtonClicked = { navController.navigate("menu") },
                onMenuButtonClicked = { navController.navigate("menu") },
                onMyButtonClicked = { navController.navigate("my") },
                onHomeButtonClicked = { navController.navigate("home") },
                onProductClicked = { imageId ->
                    // 선택한 imageId를 경로에 추가하여 productdetail로 이동
                    navController.navigate("productdetail/$imageId")
                }
            )
        }
    }
}