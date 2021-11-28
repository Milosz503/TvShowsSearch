package com.miloszglowaczewski.tvshowssearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TvShowModel(
    val id: Long,
    val title: String
)

sealed class SearchState {
    object WaitingForInput: SearchState()
    object Loading: SearchState()
    data class Data(
        val tvShows: List<TvShowModel>
    ): SearchState()
}

class SearchViewModel : ViewModel() {


    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.WaitingForInput)

    private var searchJob: Job? = null

    val state: StateFlow<SearchState>
            get() = _state

    fun search(filter: String) {
        searchJob?.cancel()

        if(filter.isEmpty()) {
            _state.value = SearchState.WaitingForInput
            return
        }

        loadTvShows(filter)
    }

    private fun loadTvShows(filter: String) {
        searchJob = viewModelScope.launch {
            _state.value = SearchState.Loading
            delay(500)
            _state.value = SearchState.Data(
                (0 until 10).map { TvShowModel(it.toLong(), "$it - $filter") }
            )
        }
    }

}