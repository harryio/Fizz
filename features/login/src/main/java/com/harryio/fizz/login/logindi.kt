package com.harryio.fizz.login

import com.harryio.fizz.authenticationrepository.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Provider
import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.BINARY)
private annotation class LoginScope

@Module(subcomponents = [LoginComponent::class])
object LoginModule

@Subcomponent(modules = [LoginAssistedInjectionModule::class])
@LoginScope
interface LoginComponent {

    fun loginViewModelFactory(): LoginViewModel.Factory

    @Subcomponent.Factory
    interface Factory {

        fun create(): LoginComponent
    }
}

interface LoginComponentProvider {

    fun provideLoginComponentFactory(): LoginComponent.Factory

}

@Module
internal object LoginAssistedInjectionModule {

    @Provides
    @LoginScope
    @JvmStatic
    fun provideLoginViewModelFactory(authenticationRepository: Provider<AuthenticationRepository>): LoginViewModel.Factory =
        LoginViewModel_AssistedFactory(authenticationRepository)
}