package com.jeflette.mystory.util

sealed class Resource<T>(
    val data: T? = null, val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T> : Resource<T>()
    class Error<T>(data: T? = null, throwable: Throwable) : Resource<T>(data, throwable)
}