package com.miloszglowaczewski.tvshowssearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miloszglowaczewski.tvshowssearch.repositories.Resource
import com.miloszglowaczewski.tvshowssearch.repositories.TvShowsRepository
import com.miloszglowaczewski.tvshowssearch.ui.search.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TvShowModel(
    val id: Long,
    val title: String,
    val genres: List<String>,
    val poster: String,
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TvShowsRepository
) : ViewModel() {

    private val minCharsToSearch = 3

    private val _state: MutableStateFlow<SearchState> =
        MutableStateFlow(SearchState.WaitingForInput)

    private var searchJob: Job? = null

    val state: StateFlow<SearchState>
        get() = _state

    fun search(filter: String) {
        searchJob?.cancel()

        if (filter.length < minCharsToSearch) {
            _state.value = SearchState.WaitingForInput
            return
        }

        loadTvShows(filter)
    }

    private fun loadTvShows(query: String) {
        searchJob = viewModelScope.launch {
            _state.value = SearchState.Loading

            // Delay search to filter out rapid query changes, avoids rate limiting
            delay(500)
            val result = repository.searchTvShows(query)
            _state.value = when (result) {
                is Resource.Failed -> SearchState.Error(result.error)
                is Resource.Success -> SearchState.Data(result.data)
            }
        }
    }


}