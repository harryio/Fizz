package com.stakkdev.fizz.login

import com.stakkdev.fizz.authenticationrepository.AuthenticationComponent
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Component(modules = [LoginModule::class], dependencies = [AuthenticationComponent::class])
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