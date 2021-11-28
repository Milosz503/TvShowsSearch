package com.miloszglowaczewski.tvshowssearch.repositories

import android.util.Log
import com.miloszglowaczewski.tvshowssearch.TvShowModel
import com.miloszglowaczewski.tvshowssearch.network.TvMazeApi
import java.net.UnknownHostException
import javax.inject.Inject

class TvShowsRepositoryImpl @Inject constructor(
    private val client: TvMazeApi
) : TvShowsRepository {

    override suspend fun searchTvShows(query: String): Resource<List<TvShowModel>> {

        try {
            val shows = client.searchTvShows(query)
                .map { TvShowModel(
                    id = it.show.id,
                    title = it.show.name
                ) }
            return Resource.Success(shows)

        } catch (e: Exception) {
            if(e is UnknownHostException) {
                return Resource.Failed("No internet connection.")
            }
            Log.e("TvShowsRepository", "Failed to search tv shows", e)
            return Resource.Failed("Unknown error, please check your internet connection.")
        }
    }

}