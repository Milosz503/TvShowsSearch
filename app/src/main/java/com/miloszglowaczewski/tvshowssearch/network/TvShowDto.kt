package com.miloszglowaczewski.tvshowssearch.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvShowSearchDto(
    val score: Float,
    val show: TvShowDto,
)

@JsonClass(generateAdapter = true)
data class TvShowDto(
    val id: Long,
    val name: String
)