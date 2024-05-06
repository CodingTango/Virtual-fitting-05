package com.example.scrollpractice.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.scrollpractice.R
import com.example.scrollpractice.data.Datasource
import com.example.scrollpractice.model.Clothes

@Composable
fun ProductScreen( modifier: Modifier = Modifier,
                   onFittingButtonClicked:()->Unit,
                   onBackButtonClicked:()->Unit){
    val clothList: List<Clothes> = Datasource().loadClothes()
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
            Button(onClick = { onFittingButtonClicked() }) {
                Text(stringResource(R.string.fitting_button))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {onBackButtonClicked()}) {
                Text(text = "Back")
            }
        }
    }
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

