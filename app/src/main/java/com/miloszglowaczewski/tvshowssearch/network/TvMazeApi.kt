package com.miloszglowaczewski.tvshowssearch.network

import retrofit2.http.GET
import retrofit2.http.Query

interface TvMazeApi {

    @GET("search/shows")
    suspend fun searchTvShows(@Query("q") query: String): List<TvShowSearchDto>

}