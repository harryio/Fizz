package com.harryio.fizz.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.HttpException
import retrofit2.Response

sealed class ApiResponse<T> {

    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            val errorStatusCode =
                parseErrorStatusCode((error as? HttpException)?.response()?.errorBody()?.string())
            val networkStatusCode = (error as? HttpException)?.code()
            return ApiErrorResponse(errorStatusCode, networkStatusCode)
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                ApiSuccessResponse(response.body())
            } else {
                ApiErrorResponse(
                    parseErrorStatusCode(response.errorBody()?.string()),
                    response.code()
                )
            }
        }
    }
}

data class ApiSuccessResponse<T>(val body: T?) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorStatusCode: Int?, val networkStatusCode: Int?) :
    ApiResponse<T>()

@JsonClass(generateAdapter = true)
internal data class ErrorResponse(@Json(name = "status_code") val statusCode: Int)

private fun parseErrorStatusCode(response: String?): Int? = if (response == null) {
    null
} else {
    NetworkInteractor.moshi.adapter(ErrorResponse::class.java).fromJson(response)?.statusCode
}