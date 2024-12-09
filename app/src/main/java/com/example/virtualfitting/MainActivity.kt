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
import com.example.virtualfitting.screens.Hoodie
import com.example.virtualfitting.screens.Menu
import com.example.virtualfitting.screens.My
import com.example.virtualfitting.screens.Product
import com.example.virtualfitting.screens.ProductDetail
import com.example.virtualfitting.screens.Shirt

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
                onMyButtonClicked = { navController.navigate("my") },
                onHomeButtonClicked = { navController.navigate("home") }
            )
        }
        composable(
            route = "productdetail/{imageId}/{csvFileName}",
            arguments = listOf(
                navArgument("imageId") { defaultValue = "" },
                navArgument("csvFileName") { defaultValue = "products.csv" }
            )
        ) { backStackEntry ->
            val imageId = backStackEntry.arguments?.getString("imageId") ?: ""
            val csvFileName = backStackEntry.arguments?.getString("csvFileName") ?: "products.csv"
            ProductDetail(
                imageId = imageId,
                csvFileName = csvFileName,
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
                onMyButtonClicked = { navController.navigate("my") },
                onHomeButtonClicked = { navController.navigate("home") },
                onNavigateToDetail = { category ->
                    when (category) {
                        "셔츠/블라우스" -> navController.navigate("product/shirt.csv")
                        "맨투맨/스웨트" -> navController.navigate("product/sweatshirt.csv")
                        "후드 티셔츠" -> navController.navigate("product/hoodie.csv")
                        else -> navController.navigate("home")
                    }
                }
            )
        }
        composable(route = "my") {
            My(
                onMenuButtonClicked = { navController.navigate("menu") },
                onMyButtonClicked = { navController.navigate("my") },
                onHomeButtonClicked = { navController.navigate("home") }
            )
        }
        composable(route = "product/{csvFileName}") { backStackEntry ->
            val csvFileName = backStackEntry.arguments?.getString("csvFileName") ?: "products.csv"
            Product(
                onBackButtonClicked = { navController.navigate("menu") },
                onMenuButtonClicked = { navController.navigate("menu") },
                onMyButtonClicked = { navController.navigate("my") },
                onHomeButtonClicked = { navController.navigate("home") },
                onProductClicked = { imageId ->
                    navController.navigate("productdetail/$imageId/$csvFileName")
                },
                csvFileName = csvFileName,
                categoryList = when (csvFileName) {
                    "sweatshirt.csv" -> listOf("전체", "BEST", "NEW", "맨투맨", "후드", "스웨트")
                    "shirt.csv" -> listOf("전체", "BEST", "NEW", "셔츠", "블라우스", "긴소매")
                    "hoodie.csv" -> listOf("전체", "BEST", "NEW", "후드", "집업", "기모")
                    else -> listOf("전체", "BEST", "NEW")
                },
                title = when (csvFileName) {
                    "sweatshirt.csv" -> "맨투맨/스웨트"
                    "shirt.csv" -> "셔츠/블라우스"
                    "hoodie.csv" -> "후드 티셔츠"
                    else -> "상품"
                }
            )
        }
    }
}
