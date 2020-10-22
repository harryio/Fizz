package com.harryio.fizz.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.harryio.fizz.authenticationrepository.AuthenticationRepository
import com.harryio.fizz.common.AuthenticationToken
import com.harryio.fizz.common.FizzNetworkException
import com.harryio.fizz.common_feature.Event
import com.harryio.fizz.common_feature.R
import com.harryio.fizz.login.R.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @Mock
    private lateinit var authenticationRepository: AuthenticationRepository

    @Mock
    private lateinit var loginButtonEnabledObserver: Observer<Boolean>

    @Mock
    private lateinit var openUrlObserver: Observer<Event<String>>

    @Mock
    private lateinit var sessionIdObserver: Observer<Event<String>>

    @Mock
    private lateinit var loginCompleteObserver: Observer<Event<Any?>>

    @Mock
    private lateinit var showLoaderObserver: Observer<Boolean>

    @Mock
    private lateinit var errorObserver : Observer<Event<Int>>

    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        loginViewModel =
            LoginViewModel(authenticationRepository, TestCoroutineDispatcher(), SavedStateHandle())
        loginViewModel.loginButtonEnabled.observeForever(loginButtonEnabledObserver)
        loginViewModel.openUrl.observeForever(openUrlObserver)
        loginViewModel.sessionIdLiveData.observeForever(sessionIdObserver)
        loginViewModel.loginCompleteLiveData.observeForever(loginCompleteObserver)
        loginViewModel.showLoader.observeForever(showLoaderObserver)
        loginViewModel.errorMsgLiveData.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        loginViewModel.loginButtonEnabled.removeObserver(loginButtonEnabledObserver)
        loginViewModel.openUrl.removeObserver(openUrlObserver)
        loginViewModel.sessionIdLiveData.removeObserver(sessionIdObserver)
        loginViewModel.loginCompleteLiveData.removeObserver(loginCompleteObserver)
        loginViewModel.showLoader.removeObserver(showLoaderObserver)
        loginViewModel.errorMsgLiveData.removeObserver(errorObserver)
    }

    @Test
    fun `test successful login with credentials`() = runBlockingTest {
        loginViewModel.username.value = "username"
        loginViewModel.password.value = "password"

        val authenticationToken = "AuthenticationToken"
        val requestToken = "RequestToken"
        val sessionId = "SessionId"
        `when`(authenticationRepository.getAuthenticationToken()).thenReturn(AuthenticationToken((authenticationToken)))
        `when`(
            authenticationRepository.createSession(
                loginViewModel.username.value!!,
                loginViewModel.password.value!!,
                authenticationToken
            )
        ).thenReturn(requestToken)
        `when`(authenticationRepository.createSession(requestToken)).thenReturn(sessionId)

        loginViewModel.handleLoginButtonClick()
        verify(showLoaderObserver).onChanged(true)
        verify(authenticationRepository).getAuthenticationToken()
        verify(authenticationRepository).createSession(
            loginViewModel.username.value!!,
            loginViewModel.password.value!!,
            authenticationToken
        )
        verify(authenticationRepository).createSession(requestToken)
        verify(showLoaderObserver).onChanged(false)
        verify(sessionIdObserver).onChanged(Event(sessionId))
        verify(loginCompleteObserver).onChanged(Event(null))
        verifyNoMoreInteractions(authenticationRepository)
        verifyNoMoreInteractions(sessionIdObserver)
        verifyNoMoreInteractions(loginCompleteObserver)
    }

    @Test
    fun `test get authentication token fetch failure when login with credentials`() = runBlockingTest {
        `when`(authenticationRepository.getAuthenticationToken()).thenThrow(IllegalStateException())
        loginViewModel.handleLoginButtonClick()
        verify(authenticationRepository).getAuthenticationToken()
        verify(showLoaderObserver).onChanged(true)
        verify(errorObserver).onChanged(Event(R.string.error_generic))
        verify(showLoaderObserver).onChanged(false)
        verifyNoMoreInteractions(authenticationRepository)
        verifyNoMoreInteractions(showLoaderObserver)
        verifyNoMoreInteractions(errorObserver)
    }

    @Test
    fun `test get request token fetch failure when login with credentials`() = runBlockingTest {
        loginViewModel.username.value="username"
        loginViewModel.password.value="password"

        val authenticationToken = "AuthenticationToken"
        `when`(authenticationRepository.getAuthenticationToken()).thenReturn(AuthenticationToken(authenticationToken))
        `when`(authenticationRepository.createSession(
                loginViewModel.username.value!!,
                loginViewModel.password.value!!,
                authenticationToken
        )).thenThrow(IllegalStateException())
        loginViewModel.handleLoginButtonClick()
        verify(authenticationRepository).getAuthenticationToken()
        verify(authenticationRepository).createSession(
                loginViewModel.username.value!!,
                loginViewModel.password.value!!,
                authenticationToken
        )
        verify(showLoaderObserver).onChanged(true)
        verify(errorObserver).onChanged(Event(R.string.error_generic))
        verify(showLoaderObserver).onChanged(false)
        verifyNoMoreInteractions(authenticationRepository)
        verifyNoMoreInteractions(showLoaderObserver)
        verifyNoMoreInteractions(errorObserver)
    }

    @Test
    fun `test create session failure when login with credentials`() = runBlockingTest {
        loginViewModel.username.value = "username"
        loginViewModel.password.value = "password"

        val authenticationToken = "AuthenticationToken"
        val requestToken = "RequestToken"
        val fizzNetworkException = FizzNetworkException(R.string.error_authentication_failed, 32, null)
        `when`(authenticationRepository.getAuthenticationToken()).thenReturn(AuthenticationToken((authenticationToken)))
        `when`(
                authenticationRepository.createSession(
                        loginViewModel.username.value!!,
                        loginViewModel.password.value!!,
                        authenticationToken
                )
        ).thenReturn(requestToken)
        `when`(authenticationRepository.createSession(requestToken)).thenThrow(fizzNetworkException)

        loginViewModel.handleLoginButtonClick()
        verify(authenticationRepository).getAuthenticationToken()
        verify(authenticationRepository).createSession(loginViewModel.username.value!!, loginViewModel.password.value!!, authenticationToken)
        verify(authenticationRepository).createSession(requestToken)
        verify(showLoaderObserver).onChanged(true)
        verify(errorObserver).onChanged(Event(fizzNetworkException.errorResId!!))
        verify(showLoaderObserver).onChanged(false)
        verifyNoMoreInteractions(authenticationRepository)
        verifyNoMoreInteractions(showLoaderObserver)
        verifyNoMoreInteractions(errorObserver)
    }

    @Test
    fun `test approved access from deeplink with success create session response`() = runBlockingTest {
        val requestToken = "requestToken"
        val sessionId = "sessionId"
        `when`(authenticationRepository.createSession(requestToken)).thenReturn(sessionId)
        loginViewModel.handleLoginDeeplinkResponse(true, requestToken)

        verify(authenticationRepository).createSession(requestToken)
        verify(showLoaderObserver).onChanged(true)
        verify(showLoaderObserver).onChanged(false)
        verify(sessionIdObserver).onChanged(Event(sessionId))
        verify(loginCompleteObserver).onChanged(Event(null))
        verifyNoMoreInteractions(authenticationRepository)
        verifyNoMoreInteractions(showLoaderObserver)
        verifyNoMoreInteractions(sessionIdObserver)
        verifyNoMoreInteractions(loginCompleteObserver)
        verifyZeroInteractions(errorObserver)
    }

    @Test
    fun `test approved access from deeplink with create session failure`() = runBlockingTest {
        val requestToken = "requestToken"
        `when`(authenticationRepository.createSession(requestToken)).thenThrow(IllegalStateException())

        loginViewModel.handleLoginDeeplinkResponse(true, requestToken)
        verify(authenticationRepository).createSession(requestToken)
        verify(showLoaderObserver).onChanged(true)
        verify(showLoaderObserver).onChanged(false)
        verify(errorObserver).onChanged(Event(R.string.error_generic))
        verifyNoMoreInteractions(authenticationRepository)
        verifyNoMoreInteractions(showLoaderObserver)
        verifyNoMoreInteractions(errorObserver)
        verifyZeroInteractions(sessionIdObserver)
        verifyZeroInteractions(loginCompleteObserver)
    }

    @Test
    fun `test deeplink response when access is not approved`() {
        loginViewModel.handleLoginDeeplinkResponse(false, "requestToken")
        verify(errorObserver).onChanged(Event(string.login_not_approved))
        verifyNoMoreInteractions(errorObserver)
        verifyZeroInteractions(showLoaderObserver)
        verifyZeroInteractions(sessionIdObserver)
        verifyZeroInteractions(loginCompleteObserver)
    }

    @Test
    fun `test successful tmdb login`() = runBlockingTest {
        val authenticationToken = "AuthenticationToken"
        `when`(authenticationRepository.getAuthenticationToken()).thenReturn(AuthenticationToken(authenticationToken))

        loginViewModel.handleTmdbLoginButtonClick()
        verify(authenticationRepository).getAuthenticationToken()
        verify(showLoaderObserver).onChanged(true)
        verify(openUrlObserver).onChanged(Event(String.format(AUTHENTICATION_URL,authenticationToken)))
        verify(showLoaderObserver).onChanged(false)
        verifyNoMoreInteractions(authenticationRepository)
        verifyNoMoreInteractions(openUrlObserver)
        verifyNoMoreInteractions(showLoaderObserver)
        verifyZeroInteractions(errorObserver)
    }

    @Test
    fun `test tmdb login failure`() = runBlockingTest {
        `when`(authenticationRepository.getAuthenticationToken()).thenThrow(IllegalStateException())
        loginViewModel.handleTmdbLoginButtonClick()
        verify(authenticationRepository).getAuthenticationToken()
        verify(showLoaderObserver).onChanged(true)
        verify(errorObserver).onChanged(Event(R.string.error_generic))
        verify(showLoaderObserver).onChanged(false)
        verifyNoMoreInteractions(authenticationRepository)
        verifyZeroInteractions(openUrlObserver)
        verifyNoMoreInteractions(showLoaderObserver)
        verifyNoMoreInteractions(errorObserver)
        verifyZeroInteractions(errorObserver)
    }

    @Test
    fun `test login button enable with various inputs`() {
        //Will be called couple of times when initial value of username and password inputs are set
        //username = "", password = ""
        verify(loginButtonEnabledObserver, times(2)).onChanged(false)

        //username = "username", password = ""
        loginViewModel.username.value = "username"
        verify(loginButtonEnabledObserver, times(3)).onChanged(false)

        //username = "username", "password" = "password"
        loginViewModel.password.value = "password"
        verify(loginButtonEnabledObserver).onChanged(true)

        //username = "", password = "password"
        loginViewModel.username.value = ""
        verify(loginButtonEnabledObserver, times(4)).onChanged(false)

        ///username = "username", password = "password"
        loginViewModel.username.value = "username"
        verify(loginButtonEnabledObserver, times(2)).onChanged(true)

        //username = "username", password = ""
        loginViewModel.password.value = ""
        verify(loginButtonEnabledObserver, times(5)).onChanged(false)

        //username = "username", password = "password"
        loginViewModel.password.value = "password"
        verify(loginButtonEnabledObserver, times(3)).onChanged(true)
    }
}