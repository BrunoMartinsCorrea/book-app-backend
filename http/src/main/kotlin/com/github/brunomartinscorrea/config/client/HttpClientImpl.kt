package com.github.brunomartinscorrea.config.client

import com.github.brunomartinscorrea.adapter.http.CircuitBreaker
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.json.defaultSerializer
import io.ktor.client.plugins.logging.LogLevel.INFO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientImpl(
    private val circuitBreaker: CircuitBreaker<HttpResponse>
) : com.github.brunomartinscorrea.adapter.http.HttpClient {

    override suspend fun <T : Any> post(endpoint: String, body: T) {
        circuitBreaker.apply {
            client.post(endpoint) {
                setBody(json.write(body))
            }
        }
    }

    companion object {
        private val json = defaultSerializer()
        private val client = HttpClient(OkHttp) {
            install(Logging) {
                level = INFO
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }

            expectSuccess = false

            HttpResponseValidator {
                validateResponse {
                }
            }
        }
    }
}
