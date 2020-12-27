package com.stakkdev.fizz.authenticationrepository

import com.stakkdev.fizz.common.AuthenticationToken
import com.stakkdev.fizz.common.FizzNetworkException
import com.stakkdev.fizz.domain.makeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface AuthenticationRepository {

    @Throws(FizzNetworkException::class)
    suspend fun getAuthenticationToken(): AuthenticationToken

    @Throws(FizzNetworkException::class)
    suspend fun createSession(
        username: String,
        password: String,
        requestToken: String
    ): String

    @Throws(FizzNetworkException::class)
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