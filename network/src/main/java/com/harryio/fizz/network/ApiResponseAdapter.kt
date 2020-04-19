package com.harryio.fizz.network

import com.squareup.moshi.Types
import io.reactivex.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class ApiResponseAdapter : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val singleReturnType = returnType as? ParameterizedType ?: return null
        val apiResponseType = getParameterUpperBound(0, singleReturnType)

        val apiResponseParameterizedType = apiResponseType as? ParameterizedType ?: return null
        val responseType = getParameterUpperBound(0, apiResponseParameterizedType)

        val responseWrapperType = Types.newParameterizedType(Response::class.java, responseType)
        val singleResponseWrapperType =
            Types.newParameterizedType(Single::class.java, responseWrapperType)

        @Suppress("UNCHECKED_CAST") val delegate = retrofit.nextCallAdapter(
            this,
            singleResponseWrapperType,
            annotations
        ) as? CallAdapter<Any?, Single<Response<*>>> ?: return null

        return object : CallAdapter<Any?, Single<ApiResponse<*>>> {
            override fun adapt(call: Call<Any?>): Single<ApiResponse<*>> {
                return delegate.adapt(call).map { ApiResponse.create(it) }
                    .onErrorReturn { ApiResponse.create(it) }
            }

            override fun responseType(): Type {
                return delegate.responseType()
            }
        }
    }
}