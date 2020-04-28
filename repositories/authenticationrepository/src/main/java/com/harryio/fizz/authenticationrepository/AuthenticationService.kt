package com.harryio.fizz.authenticationrepository

import com.harryio.fizz.network.ApiResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationService {

    @GET("/3/authentication/token/new")
    fun createAuthenticationToken(): Single<ApiResponse<AuthenticationTokenResponse>>

    @POST("/3/authentication/session/new")
    fun createSession(@Body createSessionRequest: CreateSessionRequest): Single<ApiResponse<SessionResponse>>

    @POST("/3/authentication/token/validate_with_login")
    @Headers("Use-Https: true")
    fun createSession(@Body createSessionRequest: CreateSessionWithCredentialsRequest): Single<ApiResponse<SessionResponse>>

}