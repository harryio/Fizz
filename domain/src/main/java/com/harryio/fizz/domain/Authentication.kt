package com.harryio.fizz.domain

import com.harryio.fizz.common.AuthenticationToken
import com.harryio.fizz.network.NetworkInteractor
import com.harryio.fizz.network.request.CreateSessionRequest
import com.harryio.fizz.network.request.CreateSessionWithCredentialsRequest
import io.reactivex.Single

interface AuthenticationUseCase {

    fun getAuthenticationToken(): Single<AuthenticationToken>

    fun createSession(requestToken: String): Single<String>

    fun createSession(username: String, password: String, requestToken: String): Single<String>
}

internal class AuthenticationUseCaseImpl : AuthenticationUseCase {

    private val movieService = NetworkInteractor.getMovieService(BuildConfig.API_KEY)

    override fun getAuthenticationToken(): Single<AuthenticationToken> =
        movieService.createAuthenticationToken().handleResponse()
            .map { AuthenticationToken(it.requestToken) }

    override fun createSession(requestToken: String): Single<String> =
        movieService.createSession(CreateSessionRequest(requestToken)).handleResponse()
            .map { it.sessionId }

    override fun createSession(username: String, password: String, requestToken: String) =
        movieService.createSession(
            CreateSessionWithCredentialsRequest(
                username,
                password,
                requestToken
            )
        )
            .handleResponse()
            .map { it.sessionId }

}