package com.weather

import android.app.Application
import com.weather.weatherapp.BuildConfig.DEBUG
import com.weather.weatherapp.di.injectFeatures
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (DEBUG) androidLogger()
            androidContext(this@App)
            injectFeatures()
        }
    }
}
