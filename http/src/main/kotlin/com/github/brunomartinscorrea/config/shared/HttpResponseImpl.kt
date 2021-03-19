package com.github.brunomartinscorrea.config.shared

import com.github.brunomartinscorrea.adapter.http.HttpResponse

class HttpResponseImpl<T>(
    override val statusCode: Int,
    override val headers: Map<String, String>,
    override val contentType: String,
    override val body: T?
) : HttpResponse<T>
