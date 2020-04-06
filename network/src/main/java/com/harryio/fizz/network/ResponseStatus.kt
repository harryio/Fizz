package com.harryio.fizz.network

import com.squareup.moshi.Json

internal class ResponseStatus {

    internal var success: Boolean = false

    internal @Json(name = "status_code")
    var statusCode: String? = null
}