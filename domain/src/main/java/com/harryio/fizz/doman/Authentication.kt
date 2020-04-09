package com.harryio.fizz.doman

import com.harryio.fizz.common.AuthenticationToken
import com.harryio.fizz.network.NetworkInteractor
import io.reactivex.rxjava3.core.Single

interface AuthenticationUseCase {

    fun getAuthenticationToken(): Single<AuthenticationToken>
}

internal class AuthenticationUseCaseImpl : AuthenticationUseCase {

    private val movieService = NetworkInteractor.getMovieService(BuildConfig.API_KEY)

    override fun getAuthenticationToken(): Single<AuthenticationToken> =
        movieService.createAuthenticationToken().handleResponse().map { AuthenticationToken(it.requestToken) }
}