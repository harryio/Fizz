package com.harryio.fizz.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.harryio.fizz.authenticationrepository.AuthenticationRepository
import com.harryio.fizz.common.AuthenticationToken
import com.harryio.fizz.common_feature.Event
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
    }

    @After
    fun tearDown() {
        loginViewModel.loginButtonEnabled.removeObserver(loginButtonEnabledObserver)
        loginViewModel.openUrl.removeObserver(openUrlObserver)
        loginViewModel.sessionIdLiveData.removeObserver(sessionIdObserver)
        loginViewModel.loginCompleteLiveData.removeObserver(loginCompleteObserver)
        loginViewModel.showLoader.removeObserver(showLoaderObserver)
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
        verify(authenticationRepository).getAuthenticationToken()
        verify(authenticationRepository).createSession(
            loginViewModel.username.value!!,
            loginViewModel.password.value!!,
            authenticationToken
        )
        verify(authenticationRepository).createSession(requestToken)
        verify(sessionIdObserver).onChanged(Event(sessionId))
        verify(loginCompleteObserver).onChanged(Event(null))
        verifyNoMoreInteractions(authenticationRepository)
        verifyNoMoreInteractions(sessionIdObserver)
        verifyNoMoreInteractions(loginCompleteObserver)
    }

    @Test
    fun `test login failure with credentials`() {
        // TODO: 20/10/20
    }

    @Test
    fun `test successful deeplink response`() {
        // TODO: 20/10/20
    }

    @Test
    fun `test deeplink response failure`() {
        // TODO: 20/10/20
    }

    @Test
    fun `test successful tmdb login`() {
        // TODO: 20/10/20
    }

    @Test
    fun `test tmdb login failure`() {
        // TODO: 20/10/20
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