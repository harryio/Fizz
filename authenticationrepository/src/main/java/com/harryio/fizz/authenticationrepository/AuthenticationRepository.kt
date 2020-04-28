package com.harryio.fizz.authenticationrepository

import com.harryio.fizz.common.AuthenticationToken
import com.harryio.fizz.domain.handleResponse
import io.reactivex.Single
import javax.inject.Inject

interface AuthenticationRepository {
    fun getAuthenticationToken(): Single<AuthenticationToken>

    fun createSession(requestToken: String): Single<String>

    fun createSession(username: String, password: String, requestToken: String): Single<String>
}

internal class AuthenticationRepositoryImpl @Inject constructor(private val authenticationService: AuthenticationService) :
    AuthenticationRepository {

    override fun getAuthenticationToken(): Single<AuthenticationToken> =
        authenticationService.createAuthenticationToken().handleResponse()
            .map { AuthenticationToken(it.requestToken) }

    override fun createSession(requestToken: String): Single<String> =
        authenticationService.createSession(CreateSessionRequest(requestToken)).handleResponse()
            .map { it.sessionId }

    override fun createSession(username: String, password: String, requestToken: String) =
        authenticationService.createSession(
            CreateSessionWithCredentialsRequest(
                username,
                password,
                requestToken
            )
        )
            .handleResponse()
            .map { it.sessionId }

}