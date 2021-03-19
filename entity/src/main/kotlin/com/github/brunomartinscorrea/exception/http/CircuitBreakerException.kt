package com.github.brunomartinscorrea.exception.http

class CircuitBreakerException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)
