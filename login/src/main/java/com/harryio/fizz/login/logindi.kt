package com.harryio.fizz.login

import com.harryio.fizz.authenticationrepository.AuthenticationRepository
import dagger.Module
import dagger.Subcomponent

@Module(subcomponents = [LoginComponent::class])
object LoginModule

@Subcomponent
interface LoginComponent {

    fun authenticationRepository(): AuthenticationRepository

    @Subcomponent.Factory
    interface Factory {

        fun create(): LoginComponent
    }
}

interface LoginComponentProvider {

    fun provideLoginComponentFactory(): LoginComponent.Factory

}