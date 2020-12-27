package com.stakkdev.fizz.authenticationrepository

import com.stakkdev.fizz.common.FizzNetworkException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthenticationRepositoryTest {

    @Mock
    private lateinit var authenticationService: AuthenticationService

    private lateinit var authenticationRepository: AuthenticationRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        authenticationRepository =
            AuthenticationRepositoryImpl(authenticationService, testCoroutineDispatcher)

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
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

    @Test
    fun `test generate authentication token failure`() {
        runBlocking {
            val mockResponse = MockResponse()
            val statusCode = 14
            val networkErrorCode = 404
            mockResponse.setBody(getErrorResponseString(statusCode))
            mockResponse.setResponseCode(networkErrorCode)

            mockWebServer.enqueue(mockResponse)

            val baseUrl = mockWebServer.url("/").toString()
            val networkInteractor = TestNetworkInteractor(baseUrl)
            val authenticationService =
                networkInteractor.retrofit.create(AuthenticationService::class.java)

            authenticationRepository =
                AuthenticationRepositoryImpl(authenticationService, testCoroutineDispatcher)
            try {
                authenticationRepository.getAuthenticationToken()
            } catch (e: FizzNetworkException) {
                assertEquals(networkErrorCode, e.networkStatusCode)
                assertNotNull(e.errorResId)
            }
        }
    }

    @Test
    fun `test session creation failure when user credentials are provided`() {
        runBlocking {
            val mockResponse = MockResponse()
            val statusCode = 31
            val networkErrorCode = 404
            mockResponse.setBody(getErrorResponseString(statusCode))
            mockResponse.setResponseCode(networkErrorCode)

            mockWebServer.enqueue(mockResponse)

            val baseUrl = mockWebServer.url("/").toString()
            val networkInteractor = TestNetworkInteractor(baseUrl)
            val authenticationService =
                networkInteractor.retrofit.create(AuthenticationService::class.java)

            authenticationRepository =
                AuthenticationRepositoryImpl(authenticationService, testCoroutineDispatcher)
            try {
                authenticationRepository.createSession("username", "password", "requestToken")
            } catch (e: FizzNetworkException) {
                assertEquals(networkErrorCode, e.networkStatusCode)
                assertNotNull(e.errorResId)
            }
        }
    }

    @Test
    fun `test session creation failure`() {
        runBlocking {
            val mockResponse = MockResponse()
            val statusCode = 32
            val networkErrorCode = 404
            mockResponse.setBody(getErrorResponseString(statusCode))
            mockResponse.setResponseCode(networkErrorCode)

            mockWebServer.enqueue(mockResponse)

            val baseUrl = mockWebServer.url("/").toString()
            val networkInteractor = TestNetworkInteractor(baseUrl)
            val authenticationService =
                networkInteractor.retrofit.create(AuthenticationService::class.java)

            authenticationRepository =
                AuthenticationRepositoryImpl(authenticationService, testCoroutineDispatcher)
            try {
                authenticationRepository.createSession("requestToken")
            } catch (e: FizzNetworkException) {
                assertEquals(networkErrorCode, e.networkStatusCode)
                assertNotNull(e.errorResId)
            }
        }
    }

    private fun getMockAuthenticationTokenResponse() = AuthenticationTokenResponse("requestToken")

    private fun getMockSessionResponse() = SessionResponse("SessionId")

    private fun getErrorResponseString(statusCode: Int) = "{\n" +
            "  \"status_message\": \"The resource you requested could not be found.\",\n" +
            "  \"status_code\": $statusCode\n" +
            "}"
}