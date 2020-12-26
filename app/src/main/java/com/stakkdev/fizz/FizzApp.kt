package com.stakkdev.fizz

import android.app.Application
import com.stakkdev.fizz.network.FizzNetworkInteractor

class FizzApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FizzNetworkInteractor.setup(BuildConfig.API_KEY)
    }
}