package com.example.scrollpractice

import android.app.Application
import com.example.scrollpractice.data.AppContainer
import com.example.scrollpractice.data.AppDataContainer

class ImageApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
