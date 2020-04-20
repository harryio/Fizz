package com.harryio.fizz.network

import com.harryio.fizz.network.request.CreateSessionRequest
import com.harryio.fizz.network.request.CreateSessionWithCredentialsRequest
import com.harryio.fizz.network.response.AuthenticationTokenResponse
import com.harryio.fizz.network.response.SessionResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MovieService {

    @GET("/3/authentication/token/new")
    fun createAuthenticationToken(): Single<ApiResponse<AuthenticationTokenResponse>>

    @POST("/3/authentication/session/new")
    fun createSession(createSessionRequest: CreateSessionRequest): Single<ApiResponse<SessionResponse>>

    @POST("/3/authentication/token/validate_with_login")
    @Headers("Use-Https: true")
    fun createSession(@Body createSessionRequest: CreateSessionWithCredentialsRequest): Single<ApiResponse<SessionResponse>>
}