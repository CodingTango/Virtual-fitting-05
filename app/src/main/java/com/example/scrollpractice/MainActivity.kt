package com.example.scrollpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollpractice.model.Clothes
import com.example.scrollpractice.data.Datasource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "login",
                ) {
                composable("login") {
                    LoginScreen(navController)
                }
                composable("search") {
                    SearchScreen(navController)
                }
                composable("product") {
                    ProductScreen(navController)
                }
                composable("my") {
                    MyScreen(navController)
                }
                composable("fitting") {
                    FittingScreen(navController)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Virtual Fitting Service", fontSize = 50.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "다음으로 가려면 버튼을 누르세요", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            navController.navigate("search")
        }) {
            Text(text = "Next")
        }
    }
}

@Composable
fun SearchScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "옷을 터치하면 상세페이지로 이동합니다.", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "My를 터치하면 마이페이지로 이동합니다.", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("product") }) {
            Image(
                painter = painterResource(R.drawable.image6),
                contentDescription = "cloth icon")
            //Text(text = "키뮤어 후드집업")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("My")}) {
            Text(stringResource(R.string.my))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back")
        }
    }
}

@Composable
fun ProductScreen(navController: NavController) {
    ClothList(clothList = Datasource().loadClothes(), navController = navController
    )
}

@Composable
fun MyScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "마이페이지", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.man),
            contentDescription = "User Picture",
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "카메라로 사진등록")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "앨범에서 사진등록")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back")
        }
    }
}

@Composable
fun FittingScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.man),
            contentDescription = "Virtual Fitting Result")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back")
        }
    }
}
    
    

@Preview
@Composable
private fun ClothesCardPreview() {
    ClothesCard(Clothes(R.string.cloth1, R.drawable.image1))
}


@Composable
fun ClothesCard(clothes: Clothes, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
            Text(
                text = LocalContext.current.getString(clothes.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Image(painter = painterResource(clothes.imageResourceId),
                contentDescription = stringResource(clothes.stringResourceId),
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun ClothList(clothList: List<Clothes>, navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        items(clothList) { clothes ->
            ClothesCard(
                clothes = clothes,
                modifier = Modifier.padding(8.dp)
            )
        }
        item {
            Button(onClick = { navController.navigate("fitting")}) {
                Text(stringResource(R.string.fitting_button))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text(text = "Back")
            }
        }
    }
}