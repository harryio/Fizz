package com.harryio.fizz.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Response

sealed class ApiResponse<T> {

    companion object {
        fun <T> create(error: Throwable, moshi: Moshi): ApiErrorResponse<T> {
            val errorStatusCode =
                parseErrorStatusCode(
                    (error as? HttpException)?.response()?.errorBody()?.string(),
                    moshi
                )
            val networkStatusCode = (error as? HttpException)?.code()
                return ApiErrorResponse(errorStatusCode, networkStatusCode, error)
        }

        fun <T> create(response: Response<T>, moshi: Moshi): ApiResponse<T> {
            return if (response.isSuccessful) {
                ApiSuccessResponse(response.body())
            } else {
                ApiErrorResponse(
                    parseErrorStatusCode(response.errorBody()?.string(), moshi),
                    response.code()
                )
            }
        }
    }
}

data class ApiSuccessResponse<T> internal constructor(val body: T?) : ApiResponse<T>()

data class ApiErrorResponse<T> internal constructor(
    val errorStatusCode: Int?,
    val networkStatusCode: Int?,
    val cause: Throwable? = null
) :
    ApiResponse<T>()

@JsonClass(generateAdapter = true)
internal data class ErrorResponse(@Json(name = "status_code") val statusCode: Int)

private fun parseErrorStatusCode(response: String?, moshi: Moshi): Int? = if (response == null) {
    null
} else {
    moshi.adapter(ErrorResponse::class.java).fromJson(response)?.statusCode
}