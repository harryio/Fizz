package com.harryio.fizz.doman

import com.harryio.fizz.network.ApiErrorResponse
import com.harryio.fizz.network.ApiResponse
import com.harryio.fizz.network.ApiSuccessResponse
import io.reactivex.rxjava3.core.Single

internal fun <T> Single<ApiResponse<T>>.handleResponse(
    successResponseHandler: (response: ApiSuccessResponse<T>) -> T = {
        it.body ?: throw Exception("Something went wrong")
    },
    errorResponseHandler: (response: ApiErrorResponse<T>) -> T = { throw Exception("Something went wrong") }
): Single<T> {
    return map { response ->
        when (response) {
            is ApiSuccessResponse -> successResponseHandler(response)
            is ApiErrorResponse -> errorResponseHandler(response)
        }
    }
}