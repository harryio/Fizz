package com.stakkdev.fizz.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.stakkdev.fizz.authenticationrepository.AuthenticationRepository
import com.stakkdev.common_feature.BaseViewModel
import com.stakkdev.common_feature.Event
import com.stakkdev.fizz.domain.Resource
import com.stakkdev.fizz.domain.Status
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

@VisibleForTesting
internal const val AUTHENTICATION_URL =
    "https://www.themoviedb.org/authenticate/%s?redirect_to=$LOGIN_DEEPLINK"

private const val KEY_REQUEST_TOKEN = "request_token"
private const val KEY_APPROVED = "approved"

private const val KEY_USERNAME_INPUT = "username_input"
private const val KEY_PASSWORD_INPUT = "password_input"

internal class LoginViewModel @AssistedInject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
    @Assisted
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory {

        fun create(savedStateHandle: SavedStateHandle): LoginViewModel
    }

    private val loginResource = MutableLiveData<Resource<String>>()
    private val loginObserver =
        getApiCallObserver<String> { _openUrl.value = Event(String.format(AUTHENTICATION_URL, it)) }

    private val createSessionResource = MutableLiveData<Resource<String>>()
    private val createSessionObserver = getApiCallObserver<String> {
        _sessionIdLiveData.value = Event(it)
        _loginCompleteLiveData.value = Event(null)
    }

    private val _loginButtonEnabled = MediatorLiveData<Boolean>()
    val loginButtonEnabled: LiveData<Boolean>
        get() = _loginButtonEnabled

    private val _openUrl = MutableLiveData<Event<String>>()
    internal val openUrl: LiveData<Event<String>>
        get() = _openUrl

    private val _sessionIdLiveData = MutableLiveData<Event<String>>()
    internal val sessionIdLiveData: LiveData<Event<String>>
        get() = _sessionIdLiveData

    private val _loginCompleteLiveData = MutableLiveData<Event<Any?>>()
    internal val loginCompleteLiveData: LiveData<Event<Any?>>
        get() = _loginCompleteLiveData

    private val _showLoader = MutableLiveData<Boolean>()
    internal val showLoader: LiveData<Boolean>
        get() = _showLoader

    internal val requestTokenKey
        get() = KEY_REQUEST_TOKEN
    internal val approvedKey
        get() = KEY_APPROVED

    val username = savedStateHandle.getLiveData<String>(KEY_USERNAME_INPUT, "")
    val password = savedStateHandle.getLiveData<String>(KEY_PASSWORD_INPUT, "")

    init {
        loginResource.observeForever(loginObserver)
        createSessionResource.observeForever(createSessionObserver)

        val formValidator: Observer<String> = Observer {
            _loginButtonEnabled.value =
                !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }
        _loginButtonEnabled.addSource(username, formValidator)
        _loginButtonEnabled.addSource(password, formValidator)
    }

    override fun onCleared() {
        super.onCleared()

        loginResource.removeObserver(loginObserver)
        createSessionResource.removeObserver(createSessionObserver)
        _loginButtonEnabled.removeSource(username)
        _loginButtonEnabled.removeSource(password)
    }


    fun handleLoginDeeplinkResponse(approved: Boolean, requestToken: String) {
        if (approved) {
            createSession(requestToken)
        } else {
            _errorMsgLiveData.value = Event(R.string.login_not_approved)
        }
    }

    fun handleLoginButtonClick() = viewModelScope.launch(coroutineDispatcher) {
        try {
            createSessionResource.value = Resource.loading()
            val authenticationToken = authenticationRepository.getAuthenticationToken().token
            val requestToken = authenticationRepository.createSession(
                username.value!!,
                password.value!!,
                authenticationToken
            )
            val sessionId = authenticationRepository.createSession(requestToken)
            createSessionResource.value = Resource.success(sessionId)
        } catch (exception: Exception) {
            createSessionResource.value = Resource.error(exception)
        }
    }

    fun handleTmdbLoginButtonClick() = viewModelScope.launch(coroutineDispatcher) {
        loginResource.value = Resource.loading()
        try {
            val authenticationToken = authenticationRepository.getAuthenticationToken()
            loginResource.value = Resource.success(authenticationToken.token)
        } catch (exception: Exception) {
            loginResource.value = Resource.error(exception)
        }
    }

    private fun createSession(requestToken: String) = viewModelScope.launch(coroutineDispatcher) {
        createSessionResource.value = Resource.loading()
        try {
            val sessionId = authenticationRepository.createSession(requestToken)
            createSessionResource.value = Resource.success(sessionId)
        } catch (exception: Exception) {
            createSessionResource.value = Resource.error(exception)
        }
    }

    private inline fun <reified T> getApiCallObserver(
        crossinline successAction: (T) -> Unit
    ): Observer<Resource<T>> = Observer {
        val status = it.status
        _showLoader.value = status == Status.LOADING

        when (status) {
            Status.ERROR -> getNetworkExceptionHandler()(it.throwable)
            Status.SUCCESS -> successAction(it.data!!)
            Status.LOADING, Status.EMPTY -> Unit
        }
    }
}