package com.github.brunomartinscorrea.adapter.http

interface HttpClient {
    suspend fun <T : Any> post(endpoint: String, body: T)
}
