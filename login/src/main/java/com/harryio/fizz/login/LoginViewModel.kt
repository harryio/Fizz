package com.harryio.fizz.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.harryio.fizz.common_feature.BaseViewModel
import com.harryio.fizz.common_feature.Event
import com.harryio.fizz.domain.Resource
import com.harryio.fizz.domain.Status
import com.harryio.fizz.domain.authenticationUseCase

private const val AUTHENTICATION_URL =
    "https://www.themoviedb.org/authenticate/%s?redirect_to=$LOGIN_DEEPLINK"

private const val KEY_REQUEST_TOKEN = "request_token"
private const val KEY_APPROVED = "approved"

internal class LoginViewModel : BaseViewModel() {

    private val loginResource = MutableLiveData<Resource<String>>(Resource.empty())
    private val loginObserver =
        getApiCallObserver<String> { _openUrl.value = Event(String.format(AUTHENTICATION_URL, it)) }

    private val createSessionResource = MutableLiveData<Resource<String>>(Resource.empty())
    private val createSessionObserver = getApiCallObserver<String> {
        _sessionIdLiveData.value = Event(it)
        _loginCompleteLiveData.value = Event(Unit)
    }

    val loginButtonEnabled: LiveData<Boolean>
        get() = _loginButtonEnabled
    private val _loginButtonEnabled = MutableLiveData(true)

    val loginButtonText: LiveData<Int>
        get() = _loginButtonText
    private val _loginButtonText = MutableLiveData(R.string.login)

    private val _openUrl = MutableLiveData<Event<String>>()
    internal val openUrl: LiveData<Event<String>>
        get() = _openUrl

    private val _sessionIdLiveData = MutableLiveData<Event<String>>()
    internal val sessionIdLiveData: LiveData<Event<String>>
        get() = _sessionIdLiveData

    private val _loginCompleteLiveData = MutableLiveData<Event<Unit>>()
    internal val loginCompleteLiveData: LiveData<Event<Unit>>
        get() = _loginCompleteLiveData

    internal val requestTokenKey
        get() = KEY_REQUEST_TOKEN
    internal val approvedKey
        get() = KEY_APPROVED

    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val formWatcher = MediatorLiveData<String>()

    init {
        loginResource.observeForever(loginObserver)
        createSessionResource.observeForever(createSessionObserver)

        val formValidator: Observer<String> = Observer {
            _loginButtonEnabled.value =
                !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }
        formWatcher.addSource(username, formValidator)
        formWatcher.addSource(password, formValidator)
    }

    override fun onCleared() {
        super.onCleared()

        loginResource.removeObserver(loginObserver)
        createSessionResource.removeObserver(createSessionObserver)
        formWatcher.removeSource(username)
        formWatcher.removeSource(password)
    }

    fun handleLoginDeeplinkResponse(approved: Boolean?, requestToken: String?) {
        if (approved == true) {
            if (requestToken.isNullOrEmpty()) {
                _errorMsgLiveData.value = Event(R.string.login_failed)
            } else {
                createSession(requestToken)
            }
        } else {
            _errorMsgLiveData.value = Event(R.string.login_not_approved)
        }
    }


    fun handleLoginButtonClick() {
        createSessionResource.value = Resource.loading()
        disposables.add(authenticationUseCase.getAuthenticationToken()
            .flatMap {
                authenticationUseCase.createSession(
                    username.value!!,
                    password.value!!,
                    it.token
                )
            }.subscribe({
                createSessionResource.postValue(Resource.success(it))
            }, {
                createSessionResource.postValue(Resource.error(it))
            })
        )
    }

    //TODO Implement
    private fun handleTmdbLoginButtonClick() {
        loginResource.value = Resource.loading()
        disposables.add(
            authenticationUseCase.getAuthenticationToken()
                .subscribe(
                    { authenticationToken ->
                        loginResource.postValue(Resource.success(authenticationToken.token))
                    },
                    { throwable ->
                        loginResource.postValue(Resource.error(throwable))
                    }
                )
        )
    }

    private fun createSession(requestToken: String) {
        createSessionResource.value = Resource.loading()
        disposables.add(
            authenticationUseCase.createSession(requestToken)
                .subscribe({
                    createSessionResource.postValue(Resource.success(it))
                }, { throwable ->
                    createSessionResource.postValue(Resource.error(throwable))
                })
        )
    }

    private inline fun <reified T> getApiCallObserver(
        crossinline successAction: (T) -> Unit
    ): Observer<Resource<T>> = Observer {
        val status = it.status
        _loginButtonEnabled.value =
            (status != Status.LOADING) && !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        _loginButtonText.value = if (status == Status.LOADING) {
            R.string.logging_in
        } else {
            R.string.login
        }

        when (status) {
            Status.ERROR -> getNetworkExceptionHandler()(it.throwable!!)
            Status.SUCCESS -> successAction(it.data!!)
            Status.LOADING, Status.EMPTY -> Unit
        }
    }
}