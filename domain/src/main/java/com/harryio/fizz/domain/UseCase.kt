package com.harryio.fizz.domain

val authenticationUseCase: AuthenticationUseCase by lazy {

    AuthenticationUseCaseImpl()
}