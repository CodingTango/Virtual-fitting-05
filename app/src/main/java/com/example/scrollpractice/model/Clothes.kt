package com.example.scrollpractice.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Clothes(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int
)
