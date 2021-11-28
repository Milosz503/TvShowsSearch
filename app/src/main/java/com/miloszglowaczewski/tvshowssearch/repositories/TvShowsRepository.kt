package com.miloszglowaczewski.tvshowssearch.repositories

import com.miloszglowaczewski.tvshowssearch.TvShowModel


interface TvShowsRepository {

    suspend fun searchTvShows(query: String): Resource<List<TvShowModel>>
}