package com.example.onehealth.app

import android.app.Application
import com.example.onehealth.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class OneHeathApplication: Application() {

    @Inject
    lateinit var applicationLifecycleObserver: ApplicationLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}