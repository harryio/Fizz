package com.harryio.fizz.authenticationrepository

import com.harryio.fizz.common.AuthenticationToken
import com.harryio.fizz.domain.makeApiCall

interface AuthenticationRepository {

    suspend fun getAuthenticationToken(): AuthenticationToken

    suspend fun createSession(requestToken: String): String

    suspend fun createSession(
        username: String,
        password: String,
        requestToken: String
    ): String
}

internal class AuthenticationRepositoryImpl constructor(private val authenticationService: AuthenticationService) :
    AuthenticationRepository {

    override suspend fun getAuthenticationToken() = makeApiCall {
        val createAuthenticationTokenResponse = authenticationService.createAuthenticationToken()
        AuthenticationToken(createAuthenticationTokenResponse.requestToken)
    }

    override suspend fun createSession(requestToken: String) = makeApiCall {
        authenticationService.createSession(CreateSessionRequest((requestToken))).sessionId
    }

    override suspend fun createSession(
        username: String,
        password: String,
        requestToken: String
    ): String = makeApiCall {
        authenticationService.createSession(
            CreateSessionWithCredentialsRequest(
                username,
                password,
                requestToken
            )
        ).requestToken
    }
}