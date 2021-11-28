package com.miloszglowaczewski.tvshowssearch.repositories

sealed class Resource<T> {
    data class Failed<T>(val error: String): Resource<T>()
    data class Success<T>(val data: T): Resource<T>()
}