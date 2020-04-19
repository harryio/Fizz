package com.harryio.fizz.network

import com.harryio.fizz.network.request.CreateSessionRequest
import com.harryio.fizz.network.response.AuthenticationTokenResponse
import com.harryio.fizz.network.response.SessionResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST

interface MovieService {

    @GET("/3/authentication/token/new")
    fun createAuthenticationToken(): Single<ApiResponse<AuthenticationTokenResponse>>

    @POST("/3/authentication/session/new")
    fun createSession(createSessionRequest: CreateSessionRequest): Single<ApiResponse<SessionResponse>>
}