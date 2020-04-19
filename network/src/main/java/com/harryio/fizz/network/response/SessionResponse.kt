package com.harryio.fizz.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionResponse(@Json(name = "session_id") val sessionId: String)