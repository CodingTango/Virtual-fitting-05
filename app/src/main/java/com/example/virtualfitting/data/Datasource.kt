package com.example.virtualfitting.data

import com.example.virtualfitting.R
import com.example.virtualfitting.model.Clothes

class Datasource() {
    fun loadClothes(): List<Clothes> {
        return listOf<Clothes>(
            Clothes(R.string.cloth1, R.drawable.image1),
            Clothes(R.string.cloth2, R.drawable.image2),
            Clothes(R.string.cloth3, R.drawable.image3),
            Clothes(R.string.cloth4, R.drawable.image4),
            Clothes(R.string.cloth5, R.drawable.image5),
        )

    }
}