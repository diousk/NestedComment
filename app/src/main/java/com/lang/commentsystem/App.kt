package com.lang.commentsystem

import android.app.Application
import com.lang.commentsystem.utils.FrescoInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FrescoInitializer().init(this)
    }
}