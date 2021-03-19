package com.github.brunomartinscorrea.adapter.http

interface HttpResponse<T> {
    val statusCode: Int
    val headers: Map<String, String>
    val contentType: String
    val body: T?
}
