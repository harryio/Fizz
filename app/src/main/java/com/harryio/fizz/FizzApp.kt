package com.harryio.fizz

import android.app.Application
import com.harryio.fizz.network.NetworkModule

class FizzApp : Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkModule.setup(BuildConfig.API_KEY)
    }
}