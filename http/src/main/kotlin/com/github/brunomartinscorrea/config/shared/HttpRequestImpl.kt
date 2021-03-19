package com.github.brunomartinscorrea.config.shared

import com.github.brunomartinscorrea.adapter.http.HttpRequest

class HttpRequestImpl<T>(
    override val method: String,
    override val uri: String,
    override val headers: Map<String, String>,
    override val pathVariables: Map<String, String>,
    override val body: T?
) : HttpRequest<T> {

    override fun response(
        status: Int,
        headers: Map<String, String>,
        contentType: String,
        body: T?
    ) = HttpResponseImpl(
        statusCode = status,
        headers = headers,
        contentType = contentType,
        body = body
    )
}
