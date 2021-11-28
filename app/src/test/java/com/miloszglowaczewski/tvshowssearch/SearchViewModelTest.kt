package com.miloszglowaczewski.tvshowssearch

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miloszglowaczewski.tvshowssearch.repositories.Resource
import com.miloszglowaczewski.tvshowssearch.repositories.TvShowsRepository
import com.miloszglowaczewski.tvshowssearch.ui.SearchState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var showsRepository: TvShowsRepository

    @Test
    fun testShortSearch() {
        testCoroutineRule.runBlockingTest {
            val viewModel = SearchViewModel(showsRepository)

            viewModel.search("te")
            Assert.assertEquals(viewModel.state.value, SearchState.WaitingForInput)
        }
    }

    @Test
    fun testValidSearch() {
        testCoroutineRule.runBlockingTest {

            val tvShows = emptyList<TvShowModel>()

            `when`(showsRepository.searchTvShows("test"))
                .thenReturn(Resource.Success(tvShows))
            val viewModel = SearchViewModel(showsRepository)

            viewModel.search("test")
            Assert.assertEquals(viewModel.state.value, SearchState.Data(tvShows))
        }
    }

    @Test
    fun testErrorSearch() {
        testCoroutineRule.runBlockingTest {
            val errorMsg = "error"

            `when`(showsRepository.searchTvShows("test"))
                .thenReturn(Resource.Failed(errorMsg))
            val viewModel = SearchViewModel(showsRepository)

            viewModel.search("test")
            Assert.assertEquals(viewModel.state.value, SearchState.Error(errorMsg))
        }
    }


}