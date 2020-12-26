package com.stakkdev.fizz.common

data class AuthenticationToken(val token: String)

data class Movie(
    val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val isAdult: Boolean?,
    val releaseDate: String?,
    val originalTitle: String,
    val originalLanguage: String?,
    val popularity: Float?,
    val voteCount: Long?,
    val video: Boolean?,
    val voteAverage: Float?
)