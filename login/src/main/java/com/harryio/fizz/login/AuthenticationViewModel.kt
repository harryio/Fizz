package com.harryio.fizz.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harryio.fizz.common_feature.BaseViewModel
import com.harryio.fizz.domain.authenticationUseCase
import java.net.URL

private const val KEY_REQUEST_TOKEN = "request_token"
private const val KEY_APPROVED = "approved"

class AuthenticationViewModel : BaseViewModel() {

    private val _msgLiveData = MutableLiveData<String>()
    internal val msgLiveData: LiveData<String>
        get() = _msgLiveData

    private val _sessionIdLiveData = MutableLiveData<String>()
    internal val sessionIdLiveData: LiveData<String>
        get() = _sessionIdLiveData

    fun handleDeeplinkResponse(link: String) {
        val queryParameters = URL(link).query.split("&")
        val queryMap = HashMap<String, String>()

        for (query in queryParameters.filterNotNull()) {
            val (key, value) = query.split("=", limit = 2)
            queryMap[key] = value
        }

        val approved = queryMap[KEY_APPROVED]?.toBoolean() ?: false
        if (approved) {
            val requestToken = queryMap[KEY_REQUEST_TOKEN]
            if (requestToken.isNullOrEmpty()) {
                showError(null)
            } else {
                authenticationUseCase.createSession(requestToken)

            }
        } else {
            showError(null)
        }
    }

    private fun createSession(requestToken: String) {
        disposables.add(authenticationUseCase.createSession(requestToken)
            .doOnSuccess { _sessionIdLiveData.postValue(it) }
            .ignoreElement()
            .subscribe({

            }, { showError(it.message) })
        )
    }

    private fun showError(msg: String?) {
        _msgLiveData.value = msg ?: "Something went wrong"
    }
}