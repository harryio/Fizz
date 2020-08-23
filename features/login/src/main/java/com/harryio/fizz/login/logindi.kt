package com.harryio.fizz.login

import com.harryio.fizz.authenticationrepository.AuthenticationModule
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Component(modules = [LoginModule::class, AuthenticationModule::class])
internal interface LoginComponent {

    fun loginViewModelFactory(): LoginViewModel.Factory
}

@Module(includes = [AssistedInject_LoginModule::class])
@AssistedModule
internal object LoginModule {

    @Provides
    @JvmStatic
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}