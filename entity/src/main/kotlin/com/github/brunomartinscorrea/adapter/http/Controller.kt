package com.github.brunomartinscorrea.adapter.http

import com.github.brunomartinscorrea.exception.http.HttpException

interface Controller<T> {
    suspend fun post(request: HttpRequest<T>): HttpResponse<T> =
        throw HttpException(501, "POST method not implemented!")

    suspend fun get(request: HttpRequest<T>): HttpResponse<T> =
        throw HttpException(501, "GET method not implemented!")

    suspend fun put(request: HttpRequest<T>): HttpResponse<T> =
        throw HttpException(501, "PUT method not implemented!")

    suspend fun patch(request: HttpRequest<T>): HttpResponse<T> =
        throw HttpException(501, "PATCH method not implemented!")

    suspend fun delete(request: HttpRequest<T>): HttpResponse<T> =
        throw HttpException(501, "DELETE method not implemented!")
}
