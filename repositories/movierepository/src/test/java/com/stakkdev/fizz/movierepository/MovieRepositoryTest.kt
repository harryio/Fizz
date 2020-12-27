package com.stakkdev.fizz.movierepository

import com.stakkdev.NetworkInteractor
import com.stakkdev.fizz.common.FizzNetworkException
import com.stakkdev.fizz.domain.PagedResponse
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
class MovieRepositoryTest {

    @Mock
    private lateinit var movieRepositoryService: MovieService

    private lateinit var movieRepository: MovieRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        movieRepository = MovieRepositoryImpl(movieRepositoryService, testCoroutineDispatcher)
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test successful popular movies fetch`() = runBlockingTest {
        val mockMovieResponseList = listOf(getMockMovieResponse())
        val mockMovieResponse = PagedResponse(0, mockMovieResponseList)
        `when`(movieRepositoryService.getPopularMovies()).thenReturn(mockMovieResponse)

        val mockMovieList = mockMovieResponseList
            .filter { it.isValid() }
            .map {
                it.toMovie()
            }

        val response = movieRepository.getPopularMovies()
        assertEquals(mockMovieList, response)
    }

    @Test
    fun `test failed popular movies fetch`() {
        runBlocking {
            val mockResponse = MockResponse()
            val statusCode = 14
            val networkErrorCode = 404
            mockResponse.setBody(getErrorResponseString(statusCode))
            mockResponse.setResponseCode(networkErrorCode)

            mockWebServer.enqueue(mockResponse)

            val baseUrl = mockWebServer.url("/").toString()
            val networkInteractor = TestNetworkInteractor(baseUrl)
            val authenticationService = networkInteractor.retrofit.create(MovieService::class.java)

            movieRepository = MovieRepositoryImpl(authenticationService, testCoroutineDispatcher)
            try {
                movieRepository.getPopularMovies()
            } catch (e: FizzNetworkException) {
                assertEquals(networkErrorCode, e.networkStatusCode)
                assertNotNull(e.errorResId)
            }
        }
    }
    private fun getMockMovieResponse() = MovieResponse(
        null, false, null, null, 1, null, null, "Movie Title", null, 0f, 0, true, 0f
    )

    private fun getErrorResponseString(statusCode: Int) = "{\n" +
            "  \"status_message\": \"The resource you requested could not be found.\",\n" +
            "  \"status_code\": $statusCode\n" +
            "}"
}

class TestNetworkInteractor(apiUrl: String) : NetworkInteractor(apiUrl)