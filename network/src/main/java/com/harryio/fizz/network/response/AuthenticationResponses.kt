package com.harryio.fizz.network.response

import com.squareup.moshi.Json

data class AuthenticationTokenResponse(@Json(name = "request_token") val requestToken: String)