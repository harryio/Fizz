package com.harryio.fizz

import com.harryio.fizz.login.LoginComponent
import com.harryio.fizz.login.LoginModule
import com.harryio.fizz.network.ApiKey
import com.harryio.fizz.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, LoginModule::class])
@Singleton
interface ApplicationComponent {

    fun loginComponentFactory(): LoginComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance @ApiKey apiKey: String): ApplicationComponent
    }
}