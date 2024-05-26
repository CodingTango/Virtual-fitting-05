package com.example.scrollpractice.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val imageRepository: ImageRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineImageRepository]
 */
class AppDataContainer(context: Context) : AppContainer {
    /**
     * Implementation for [ImageRepository]
     */
    override val imageRepository: ImageRepository by lazy {
        OfflineImageRepository(ImageDatabase.getDatabase(context).imageDao())
    }
}
