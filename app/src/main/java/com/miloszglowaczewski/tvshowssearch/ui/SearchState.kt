package com.miloszglowaczewski.tvshowssearch.ui

import com.miloszglowaczewski.tvshowssearch.TvShowModel

sealed class SearchState {
    object WaitingForInput : SearchState()
    object Loading : SearchState()
    data class Error(val error: String): SearchState()
    data class Data(
        val tvShows: List<TvShowModel>
    ) : SearchState()
}