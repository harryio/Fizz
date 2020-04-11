package com.harryio.fizz.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.harryio.fizz.common_feature.BaseViewModel
import com.harryio.fizz.doman.Resource
import com.harryio.fizz.doman.Status
import com.harryio.fizz.doman.authenticationUseCase

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

    fun handleLoginButtonClick() {
        loginObserver.value = Resource.loading()
        disposables.add(
            authenticationUseCase.getAuthenticationToken()
                .subscribe(
                    { authenticationToken ->
                        loginObserver.postValue(Resource.success(authenticationToken.token))
                    },
                    { throwable ->
                        loginObserver.postValue(
                            Resource.error(
                                throwable.message ?: "Something went wrong"
                            )
                        )
                    }
                )
        )
    }
}