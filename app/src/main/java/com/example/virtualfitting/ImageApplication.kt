package com.example.virtualfitting

import android.app.Application
import com.example.virtualfitting.data.AppContainer
import com.example.virtualfitting.data.AppDataContainer

class ImageApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
