package com.harryio.fizz

import android.app.Application
import com.harryio.fizz.login.LoginComponent
import com.harryio.fizz.login.LoginComponentProvider

class FizzApp : Application(), LoginComponentProvider {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory()
            .create(BuildConfig.API_KEY)
    }

    override fun provideLoginComponentFactory(): LoginComponent.Factory =
        applicationComponent.loginComponentFactory()
}