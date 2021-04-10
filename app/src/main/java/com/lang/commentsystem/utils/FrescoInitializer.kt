package com.lang.commentsystem.utils

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.disk.NoOpDiskTrimmableRegistry
import com.facebook.common.memory.MemoryTrimType
import com.facebook.common.memory.MemoryTrimmableRegistry
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineFactory

class FrescoInitializer {

    fun init(application: Application) {
        // setup low memory listener
        application.registerComponentCallbacks(componentCallback())

        val diskCacheConfig = DiskCacheConfig.newBuilder(application)
            .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
            .build()

        val smallImageDiskCacheConfig = DiskCacheConfig.newBuilder(application)
            .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
            .build()

        val trimRegistry = memoryTrimRegistry()

        Fresco.initialize(application)
    }

    private fun memoryTrimRegistry(): MemoryTrimmableRegistry {

        val memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance()

        memoryTrimmableRegistry.registerMemoryTrimmable { trimType ->

            when (trimType.suggestedTrimRatio) {
                MemoryTrimType.OnCloseToDalvikHeapLimit.suggestedTrimRatio,
                MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.suggestedTrimRatio,
                MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.suggestedTrimRatio ->
                    clearMemoryCaches()
            }
        }

        return memoryTrimmableRegistry
    }

    private fun componentCallback(): ComponentCallbacks2 {

        return object : ComponentCallbacks2 {

            override fun onLowMemory() {
                clearMemoryCaches()
                System.gc()
            }

            override fun onConfigurationChanged(newConfig: Configuration) {
            }

            override fun onTrimMemory(level: Int) {
                if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
                    clearMemoryCaches()
                }
            }
        }
    }

    fun clearMemoryCaches() {
        try {
            ImagePipelineFactory.getInstance().imagePipeline.clearMemoryCaches()
        } catch (e: Exception) {
        }
    }
}