package com.harryio.fizz.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateSessionRequest(@Json(name = "request_token") val requestToken: String)