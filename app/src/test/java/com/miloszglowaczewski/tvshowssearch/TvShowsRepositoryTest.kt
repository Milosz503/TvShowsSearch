package com.miloszglowaczewski.tvshowssearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miloszglowaczewski.tvshowssearch.network.TvMazeApi
import com.miloszglowaczewski.tvshowssearch.repositories.Resource
import com.miloszglowaczewski.tvshowssearch.repositories.TvShowsRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TvShowsRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var client: TvMazeApi

    @Test
    fun testValidSearch() {
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(client.searchTvShows("test"))
                .thenReturn(emptyList())

            val result = TvShowsRepositoryImpl(client).searchTvShows("test")
            Assert.assertEquals(result, Resource.Success(emptyList<TvShowModel>()))
        }
    }

    @Test
    fun testErrorSearch() {
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(client.searchTvShows("test"))
                .then { throw UnknownHostException() }

            val result = TvShowsRepositoryImpl(client).searchTvShows("test")
            Assert.assertTrue(result is Resource.Failed)
        }
    }

}