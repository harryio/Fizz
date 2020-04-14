package com.harryio.fizz.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.harryio.fizz.common_feature.BaseViewModel
import com.harryio.fizz.common_feature.Event
import com.harryio.fizz.domain.Resource
import com.harryio.fizz.domain.Status
import com.harryio.fizz.domain.authenticationUseCase

private const val AUTHENTICATION_URL =
    "https://www.themoviedb.org/authenticate/%s?redirect_to=$LOGIN_DEEPLINK"

internal class LoginViewModel : BaseViewModel() {

    private val loginObserver: MutableLiveData<Resource<String>> = MutableLiveData(Resource.empty())

    val loginButtonEnabled = Transformations.map(loginObserver) {
        it.status != Status.LOADING
    }

    val loginButtonText = Transformations.map(loginObserver) {
        if (it.status == Status.LOADING) {
            R.string.logging_in
        } else {
            R.string.login
        }
    }

    private val _openUrl = MediatorLiveData<Event<String>>()
    val openUrl: LiveData<Event<String>>
        get() = _openUrl

    init {
        _openUrl.addSource(loginObserver) {
            if (it.status == Status.SUCCESS) {
                _openUrl.value = Event(String.format(AUTHENTICATION_URL, it.data!!))
            }
        }
    }

    fun handleLoginButtonClick() {
        loginObserver.value = Resource.loading()
        disposables.add(
            authenticationUseCase.getAuthenticationToken()
                .subscribe(
                    { authenticationToken ->
                        loginObserver.postValue(Resource.success(authenticationToken.token))
                    },
                    { throwable ->
                        loginObserver.postValue(Resource.error(throwable.message))
                    }
                )
        )
    }
}