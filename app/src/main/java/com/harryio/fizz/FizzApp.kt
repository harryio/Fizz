package com.harryio.fizz

import android.app.Application
import com.harryio.fizz.network.FizzNetworkInteractor

class FizzApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FizzNetworkInteractor.setup(BuildConfig.API_KEY)
    }
}