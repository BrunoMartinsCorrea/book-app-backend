package com.github.brunomartinscorrea.adapter.http

interface CircuitBreaker<T> {
    suspend fun apply(function: suspend () -> T): T
}
