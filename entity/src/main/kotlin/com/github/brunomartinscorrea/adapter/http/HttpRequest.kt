package com.github.brunomartinscorrea.adapter.http

interface HttpRequest<T> {
    val method: String
    val uri: String
    val headers: Map<String, String>
    val pathVariables: Map<String, String>
    val body: T?

    fun response(
        status: Int = 200,
        headers: Map<String, String> = emptyMap(),
        contentType: String = "application/json",
        body: T? = null
    ): HttpResponse<T>
}
