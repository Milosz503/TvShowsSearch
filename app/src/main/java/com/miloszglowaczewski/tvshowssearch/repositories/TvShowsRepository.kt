package com.miloszglowaczewski.tvshowssearch.repositories

import android.util.Log
import com.miloszglowaczewski.tvshowssearch.TvShowModel
import com.miloszglowaczewski.tvshowssearch.network.TvMazeApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.UnknownHostException


class TvShowsRepository {

    suspend fun searchTvShows(query: String): Resource<List<TvShowModel>> {

        try {
            val shows = createClient().searchTvShows(query)
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

    private fun createClient(): TvMazeApi {

        val mosh = Moshi.Builder().build()

        return Retrofit.Builder()
            .baseUrl("https://api.tvmaze.com")
            .addConverterFactory(MoshiConverterFactory.create(mosh))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.HEADERS
                        })
                    .build()
            )
            .build()
            .create(TvMazeApi::class.java)
    }

}