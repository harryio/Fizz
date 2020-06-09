package com.harryio.fizz.login

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.Subcomponent

@Module(subcomponents = [LoginComponent::class])
object LoginModule

@Subcomponent(modules = [LoginAssistedInjectionModule::class])
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

@AssistedModule
@Module(includes = [AssistedInject_LoginAssistedInjectionModule::class])
internal object LoginAssistedInjectionModule