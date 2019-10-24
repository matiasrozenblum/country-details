package mrozenblum.repository.rest

interface Callback<T> {
    fun onComplete(value: T)
    fun onError(error: Throwable)
}