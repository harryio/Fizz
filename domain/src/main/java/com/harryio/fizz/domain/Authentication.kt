package com.harryio.fizz.domain

import com.harryio.fizz.common.AuthenticationToken
import com.harryio.fizz.network.NetworkInteractor
import com.harryio.fizz.network.request.CreateSessionRequest
import io.reactivex.Single

interface AuthenticationUseCase {

    fun getAuthenticationToken(): Single<AuthenticationToken>

    fun createSessionToken(requestToken: String): Single<String>
}

internal class AuthenticationUseCaseImpl : AuthenticationUseCase {

    private val movieService = NetworkInteractor.getMovieService(BuildConfig.API_KEY)

    override fun getAuthenticationToken(): Single<AuthenticationToken> =
        movieService.createAuthenticationToken().handleResponse()
            .map { AuthenticationToken(it.requestToken) }

    override fun createSessionToken(requestToken: String): Single<String> =
        movieService.createSession(CreateSessionRequest(requestToken)).handleResponse()
            .map { it.sessionId }
}