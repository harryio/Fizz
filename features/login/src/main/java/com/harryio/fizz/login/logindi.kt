package com.harryio.fizz.login

import com.harryio.fizz.authenticationrepository.AuthenticationModule
import com.harryio.fizz.authenticationrepository.AuthenticationRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Provider

@Component(modules = [LoginModule::class, AuthenticationModule::class])
internal interface LoginComponent {

    fun loginViewModelFactory(): LoginViewModel.Factory
}

@Module
internal object LoginModule {

    @Provides
    @JvmStatic
    fun provideLoginViewModelFactory(authenticationRepository: Provider<AuthenticationRepository>): LoginViewModel.Factory =
        LoginViewModel_AssistedFactory(authenticationRepository)
}