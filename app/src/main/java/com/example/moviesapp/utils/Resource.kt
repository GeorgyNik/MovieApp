package com.example.moviesapp.utils

import android.os.Message

sealed class Resource<T>(
    val data: T? = null,
    message: String? = null
) {
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String): Resource<T>(data,message)
    class Loading<T>(loading: Boolean = true): Resource<T>()
}