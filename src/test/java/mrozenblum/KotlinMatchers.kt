package mrozenblum

import org.mockito.Mockito

inline fun <reified T: Any> mock() = Mockito.mock(T::class.java)

fun <T> kotlinAny(): T {
    Mockito.any<T>()
    return uninitialized()
}
fun <T> uninitialized(): T = null as T
