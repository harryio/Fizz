package com.stakkdev.fizz.common

class FizzNetworkException(
    val errorResId: Int?,
    val networkStatusCode: Int?,
    throwable: Throwable? = null
) : Exception(throwable)