package com.stakkdev.fizz.authenticationrepository

import com.stakkdev.fizz.network.FizzNetworkInteractor
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
internal annotation class InternalApi

@Component(modules = [AuthenticationModule::class])
interface AuthenticationComponent {

    fun providesAuthenticationRepository(): AuthenticationRepository
}

@Module
internal object AuthenticationModule {

    @Provides
    @JvmStatic
    fun providesAuthenticationService(): AuthenticationService =
        FizzNetworkInteractor.retrofit.create(AuthenticationService::class.java)

    @Provides
    @JvmStatic
    fun providesAuthenticationRepository(authenticationService: AuthenticationService): AuthenticationRepository =
        AuthenticationRepositoryImpl(authenticationService, Dispatchers.IO)
}