package com.stakkdev.movierepository

import com.harryio.fizz.common.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MovieResponse(
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "adult") val isAdult: Boolean?,
    @Json(name = "overview") val overview: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "id") val id: Long?,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "original_language") val originalLanguage: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "popularity") val popularity: Float?,
    @Json(name = "vote_count") val voteCount: Long?,
    @Json(name = "video") val video: Boolean?,
    @Json(name = "vote_average") val voteAverage: Float?
) {

    fun isValid(): Boolean = (id != null) && !title.isNullOrEmpty() && !overview.isNullOrEmpty()

    fun toMovie() = Movie(
        id!!,
        title!!,
        overview!!,
        posterPath,
        backdropPath,
        isAdult,
        releaseDate,
        originalTitle ?: title,
        originalLanguage,
        popularity,
        voteCount,
        video,
        voteAverage
    )
}