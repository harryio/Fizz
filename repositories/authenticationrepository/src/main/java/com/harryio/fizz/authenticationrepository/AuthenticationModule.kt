package com.harryio.fizz.authenticationrepository

import com.harryio.fizz.network.NetworkModule.retrofit
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi

@Module
object AuthenticationModule {

    @Provides
    @JvmStatic
    @InternalApi
    fun providesAuthenticationService(): AuthenticationService =
        retrofit.create(AuthenticationService::class.java)

    @Provides
    @JvmStatic
    fun providesAuthenticationRepository(@InternalApi authenticationService: AuthenticationService):
            AuthenticationRepository = AuthenticationRepositoryImpl(authenticationService)
}