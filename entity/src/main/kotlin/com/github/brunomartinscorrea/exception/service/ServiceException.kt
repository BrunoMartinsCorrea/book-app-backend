package com.github.brunomartinscorrea.exception.service

class ServiceException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)
