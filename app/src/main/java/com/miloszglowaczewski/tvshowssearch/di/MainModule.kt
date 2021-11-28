package com.miloszglowaczewski.tvshowssearch.di

import com.miloszglowaczewski.tvshowssearch.network.TvMazeApi
import com.miloszglowaczewski.tvshowssearch.repositories.TvShowsRepository
import com.miloszglowaczewski.tvshowssearch.repositories.TvShowsRepositoryImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Singleton
    @Provides
    fun provideTvMazeApi(moshi: Moshi): TvMazeApi {

        return Retrofit.Builder()
            .baseUrl("https://api.tvmaze.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
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

    @Provides
    fun bindsTvShowsRepository(repositoryImpl: TvShowsRepositoryImpl): TvShowsRepository {
        return repositoryImpl
    }
}