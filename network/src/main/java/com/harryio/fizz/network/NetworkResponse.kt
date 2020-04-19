package com.harryio.fizz.network

class NetworkResponse<T> internal constructor(val response: T?, val errorStatusCode: String?) {

    fun isSuccessful() = errorStatusCode.isNullOrEmpty()
}