package com.harryio.fizz.authenticationrepository

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

internal interface AuthenticationService {

    @GET("/3/authentication/token/new")
    suspend fun createAuthenticationToken(): AuthenticationTokenResponse

    @POST("/3/authentication/session/new")
    suspend fun createSession(@Body createSessionRequest: CreateSessionRequest): SessionResponse

    @POST("/3/authentication/token/validate_with_login")
    @Headers("Use-Https: true")
    suspend fun createSession(@Body createSessionRequest: CreateSessionWithCredentialsRequest): AuthenticationTokenResponse
}