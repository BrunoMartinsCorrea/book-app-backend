package com.github.brunomartinscorrea.exception.http

class HttpException(
    val statusCode: Int,
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)
