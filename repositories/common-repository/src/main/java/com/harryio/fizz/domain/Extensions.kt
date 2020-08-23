package com.harryio.fizz.domain

import com.harryio.fizz.common.FizzNetworkException
import com.harryio.fizz.network.ApiErrorResponse
import com.harryio.fizz.network.ApiResponse
import com.harryio.fizz.network.ApiSuccessResponse
import com.harryio.fizz.network.NetworkModule
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

fun <T> ApiErrorResponse<T>.errorMsgResId(): Int? = when (errorStatusCode) {
    8 -> R.string.error_duplicated_entry
    9 -> R.string.error_service_offline
    14 -> R.string.error_authentication_failed
    30 -> R.string.error_invalid_login_credentials
    31 -> R.string.error_account_disabled
    32 -> R.string.error_account_not_verified
    33 -> R.string.error_invalid_request_token
    34 -> R.string.error_resource_unavailable
    else -> null
}


internal fun ErrorResponse?.errorMsgResId(): Int? = when (this?.statusCode) {
    8 -> R.string.error_duplicated_entry
    9 -> R.string.error_service_offline
    14 -> R.string.error_authentication_failed
    26, 30 -> R.string.error_invalid_login_credentials
    31 -> R.string.error_account_disabled
    32 -> R.string.error_account_not_verified
    33 -> R.string.error_invalid_request_token
    34 -> R.string.error_resource_unavailable
    else -> null
}

inline fun <T> Single<ApiResponse<T>>.handleResponse(
    crossinline successResponseHandler: (response: ApiSuccessResponse<T>) -> T = {
        it.body ?: throw Exception("Received empty/null response for successful call")
    },
    crossinline errorResponseHandler: (response: ApiErrorResponse<T>) -> T = {
        throw FizzNetworkException(
            it.errorMsgResId(),
            it.networkStatusCode,
            it.cause
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

suspend fun <T> makeApiCall(apiCall: suspend () -> T): T {
    try {
        return apiCall()
    } catch (httpException: HttpException) {
        withContext<T>(Dispatchers.Default) {
            val errorResponse = httpException.response()?.errorBody()?.source()?.let {
                // TODO: 22/08/20 investigate
                NetworkModule.moshi.adapter(ErrorResponse::class.java).fromJson(it)
            }
            val networkStatusCode = (httpException as? HttpException)?.code()
            throw FizzNetworkException(
                errorResponse.errorMsgResId(),
                networkStatusCode,
                httpException
            )
        }
    }
}

@JsonClass(generateAdapter = true)
internal data class ErrorResponse(@Json(name = "status_code") val statusCode: Int)