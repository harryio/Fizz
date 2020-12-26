package com.stakkdev.fizz.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PagedResponse<T>(
    @Json(name = "page") val page: Int,
    @Json(name = "results") val results: List<T>
)