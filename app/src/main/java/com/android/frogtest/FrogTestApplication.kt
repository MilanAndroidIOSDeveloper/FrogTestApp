package com.android.frogtest

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FrogTestApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        FirebaseApp.initializeApp(appContext);

    }

    companion object {
        lateinit var appContext: Context
    }


}