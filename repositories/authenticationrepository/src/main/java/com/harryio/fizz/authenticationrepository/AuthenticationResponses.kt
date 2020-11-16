package com.harryio.fizz.authenticationrepository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class AuthenticationTokenResponse(@Json(name = "request_token") val requestToken: String)

@JsonClass(generateAdapter = true)
internal data class SessionResponse(@Json(name = "session_id") val sessionId: String)