package com.harryio.fizz

import android.app.Application
import com.harryio.fizz.network.NetworkInteractor

class FizzApp : Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkInteractor.setup(BuildConfig.API_KEY)
    }
}