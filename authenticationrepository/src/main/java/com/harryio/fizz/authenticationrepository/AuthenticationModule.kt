package com.harryio.fizz.authenticationrepository

import dagger.*
import retrofit2.Retrofit

@Module(
    subcomponents = [AuthenticationComponent::class],
    includes = [AuthenticationInternalModule::class]
)
object AuthenticationModule

@Subcomponent
internal interface AuthenticationComponent {

    fun authenticationRepository(): AuthenticationRepository

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance retrofit: Retrofit): AuthenticationComponent
    }
}

@Module
internal abstract class AuthenticationInternalModule {

    @Binds
    abstract fun bindsAuthenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    companion object {
        @Provides
        @JvmStatic
        fun providesAuthenticationService(retrofit: Retrofit): AuthenticationService =
            retrofit.create(AuthenticationService::class.java)
    }
}