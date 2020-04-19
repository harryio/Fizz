package com.harryio.fizz.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticationTokenResponse(@Json(name = "request_token") val requestToken: String)