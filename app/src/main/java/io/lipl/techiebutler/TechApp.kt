package io.lipl.techiebutler

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TechApp : Application(){

    override fun onCreate() {
        super.onCreate()

    }
}
