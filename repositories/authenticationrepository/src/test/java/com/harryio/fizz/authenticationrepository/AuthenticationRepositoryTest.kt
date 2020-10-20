package com.harryio.fizz.authenticationrepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthenticationRepositoryTest {

    @Mock
    private lateinit var authenticationService: AuthenticationService

    private lateinit var authenticationRepository: AuthenticationRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        authenticationRepository =
            AuthenticationRepositoryImpl(authenticationService, testCoroutineDispatcher)
    }

    @Test
    fun `will generate a authentication token on success case`() = runBlockingTest {
        val mockResponse = getMockAuthenticationTokenResponse()
        `when`(authenticationService.createAuthenticationToken()).thenReturn(
            mockResponse
        )

        val response = authenticationRepository.getAuthenticationToken()
        assertEquals(mockResponse.requestToken, response.token)
    }

    @Test
    fun `will create a successful session with users credentials`() = runBlockingTest {
        val mockTokenResponse = getMockAuthenticationTokenResponse()
        val username = "username"
        val password = "password"
        val requestToken = "requestToken"
        `when`(
            authenticationService.createSession(
                CreateSessionWithCredentialsRequest(
                    username,
                    password,
                    requestToken
                )
            )
        ).thenReturn(mockTokenResponse)

        val response = authenticationRepository.createSession(username, password, requestToken)
        assertEquals(mockTokenResponse.requestToken, response)
    }

    @Test
    fun `will create a successful session on success case`() = runBlockingTest {
        val mockSessionResponse = getMockSessionResponse()
        `when`(authenticationService.createSession(CreateSessionRequest("RequestToken"))).thenReturn(
            mockSessionResponse
        )

        val response = authenticationRepository.createSession("RequestToken")
        assertEquals(mockSessionResponse.sessionId, response)
    }

    // TODO: 19/10/20 Test error cases

    private fun getMockAuthenticationTokenResponse() = AuthenticationTokenResponse("requestToken")

    private fun getMockSessionResponse() = SessionResponse("SessionId")
}