package com.patriciabajureanu.metgallery

import android.app.Application
import com.patriciabajureanu.metgallery.di.koinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(koinModule)
        }
    }
}
