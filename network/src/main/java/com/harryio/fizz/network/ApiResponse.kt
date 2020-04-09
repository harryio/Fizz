package com.harryio.fizz.network

import com.harryio.fizz.common.GENERIC_ERROR
import com.squareup.moshi.Json
import retrofit2.HttpException
import retrofit2.Response

sealed class ApiResponse<T> {

    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            val errorStatusCode = if (error is HttpException) {
                parseErrorStatusCode(error.response()?.errorBody()?.string())
                    ?: error.code().toString()
            } else {
                GENERIC_ERROR
            }

            return ApiErrorResponse(errorStatusCode)
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                ApiSuccessResponse(response.body())
            } else {
                ApiErrorResponse(
                    parseErrorStatusCode(response.errorBody()?.string())
                        ?: response.code().toString()
                )
            }
        }
    }
}

data class ApiSuccessResponse<T>(val body: T?) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorStatusCode: String) : ApiResponse<T>()

private data class ErrorResponse(@Json(name = "status_code") val statusCode: String)

private fun parseErrorStatusCode(response: String?): String? = if (response == null) {
    null
} else {
    NetworkInteractor.moshi.adapter<ErrorResponse>(ErrorResponse::class.java)
        .fromJson(response)?.statusCode
}