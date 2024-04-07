package com.example.scrollpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrollpractice.ui.theme.ScrollPracticeTheme
import com.example.scrollpractice.model.Clothes
import com.example.scrollpractice.data.Datasource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrollPracticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ClothesApp()
                }
            }
        }
    }
}

@Preview
@Composable
private fun ClothesCardPreview() {
    ClothesCard(Clothes(R.string.cloth1, R.drawable.image1))
}

@Composable
fun ClothesApp() {
    ClothList(clothList = Datasource().loadClothes(),
        )
}

@Composable
fun ClothesCard(clothes: Clothes, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
            Image(painter = painterResource(clothes.imageResourceId),
                contentDescription = stringResource(clothes.stringResourceId),
                modifier = Modifier
                    .fillMaxSize(),
                //contentScale = ContentScale.Crop
                )
            Text(
                text = LocalContext.current.getString(clothes.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun ClothList(clothList: List<Clothes>, modifier: Modifier = Modifier) {
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
                Button(onClick = { /*TODO*/ }) {
                    Text(stringResource(R.string.fitting_button))
                }
            }
        }
}