package com.harryio.fizz.login

import com.harryio.fizz.common_feature.BaseViewModel
import com.harryio.fizz.doman.authenticationUseCase

internal class LoginViewModel : BaseViewModel() {

    fun handleLoginButtonClick() {
        disposables.add(
            authenticationUseCase.getAuthenticationToken().subscribe(
                {},
                Throwable::printStackTrace
            )
        )
    }
}