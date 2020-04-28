package com.harryio.fizz.authenticationrepository

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi

@Module
object AuthenticationModule {

    @Provides
    @JvmStatic
    @InternalApi
    fun providesAuthenticationService(retrofit: Retrofit): AuthenticationService =
        retrofit.create(AuthenticationService::class.java)

    @Provides
    @Singleton
    fun providesAuthenticationRepository(@InternalApi authenticationService: AuthenticationService): AuthenticationRepository =
        AuthenticationRepositoryImpl(authenticationService)
}