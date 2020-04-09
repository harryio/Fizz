package com.harryio.fizz.network

import com.harryio.fizz.network.response.AuthenticationTokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface MovieService {

    @GET("/3/authentication/token/new")
    fun createAuthenticationToken() : Single<ApiResponse<AuthenticationTokenResponse>>
}