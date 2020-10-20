package com.harryio.fizz.authenticationrepository

import com.harryio.fizz.common.AuthenticationToken
import com.harryio.fizz.domain.makeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface AuthenticationRepository {

    suspend fun getAuthenticationToken(): AuthenticationToken

    suspend fun createSession(
        username: String,
        password: String,
        requestToken: String
    ): String

    suspend fun createSession(requestToken: String): String
}

internal class AuthenticationRepositoryImpl @Inject constructor(
    @InternalApi
    private val authenticationService: AuthenticationService,
    private val coroutineDispatcher: CoroutineDispatcher
) : AuthenticationRepository {

    override suspend fun getAuthenticationToken() = makeApiCall(coroutineDispatcher) {
        val createAuthenticationTokenResponse = authenticationService.createAuthenticationToken()
        AuthenticationToken(createAuthenticationTokenResponse.requestToken)
    }

    override suspend fun createSession(
        username: String,
        password: String,
        requestToken: String
    ): String = makeApiCall(coroutineDispatcher) {
        authenticationService.createSession(
            CreateSessionWithCredentialsRequest(
                username,
                password,
                requestToken
            )
        ).requestToken
    }

    override suspend fun createSession(requestToken: String) = makeApiCall(coroutineDispatcher) {
        authenticationService.createSession(CreateSessionRequest((requestToken))).sessionId
    }
}