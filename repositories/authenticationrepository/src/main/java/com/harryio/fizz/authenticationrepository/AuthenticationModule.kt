package com.harryio.fizz.authenticationrepository

import com.harryio.fizz.network.NetworkInteractor.retrofit
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
internal annotation class InternalApi

@Module
object AuthenticationModule {

    @Provides
    @JvmStatic
    @InternalApi
    fun providesAuthenticationService(): AuthenticationService =
        retrofit.create(AuthenticationService::class.java)

    @Provides
    @JvmStatic
    fun providesAuthenticationRepository(
        @InternalApi authenticationService: AuthenticationService
    ): AuthenticationRepository =
        AuthenticationRepositoryImpl(authenticationService, Dispatchers.IO)
}