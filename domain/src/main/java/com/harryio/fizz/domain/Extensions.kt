package com.harryio.fizz.domain

import com.harryio.fizz.common.FizzNetworkException
import com.harryio.fizz.network.ApiErrorResponse
import com.harryio.fizz.network.ApiResponse
import com.harryio.fizz.network.ApiSuccessResponse
import io.reactivex.Single

internal fun <T> ApiErrorResponse<T>.errorMsgResId(): Int? = when (errorStatusCode) {
    8 -> R.string.error_duplicated_entry
    9 -> R.string.error_service_offline
    14 -> R.string.error_authentication_failed
    31 -> R.string.error_account_disabled
    32 -> R.string.error_account_not_verified
    33 -> R.string.error_invalid_request_token
    34 -> R.string.error_resource_unavailable
    else -> null
}

internal fun <T> Single<ApiResponse<T>>.handleResponse(
    successResponseHandler: (response: ApiSuccessResponse<T>) -> T = {
        it.body ?: throw Exception("Received empty/null response for successful call")
    },
    errorResponseHandler: (response: ApiErrorResponse<T>) -> T = {
        throw FizzNetworkException(
            it.errorMsgResId(),
            it.networkStatusCode
        )
    }
): Single<T> {
    return map { response ->
        when (response) {
            is ApiSuccessResponse -> successResponseHandler(response)
            is ApiErrorResponse -> errorResponseHandler(response)
        }
    }
}