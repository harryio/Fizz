package com.stakkdev.fizz.authenticationrepository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateSessionRequest(@Json(name = "request_token") val requestToken: String)

@JsonClass(generateAdapter = true)
data class CreateSessionWithCredentialsRequest(
    val username: String,
    val password: String,
    @Json(name = "request_token") val requestToken: String
)