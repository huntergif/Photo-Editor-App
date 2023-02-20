package com.aqchen.filterfiesta.util

// Sealed class since we only want 3 classes/object to extend - no other classes should
// extend Resource.
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    // Error could still return data such as cached data
    data class Error<out T>(val errorMessage: String, val data: T? = null) : Resource<T>()
    object Loading: Resource<Nothing>()
}