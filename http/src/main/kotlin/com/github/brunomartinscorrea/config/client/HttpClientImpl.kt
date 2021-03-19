package com.github.brunomartinscorrea.config.client

import com.github.brunomartinscorrea.adapter.http.CircuitBreaker
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel.INFO
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json

class HttpClientImpl(
    private val circuitBreaker: CircuitBreaker<HttpResponse>
) : com.github.brunomartinscorrea.adapter.http.HttpClient {

    override suspend fun <T : Any> post(endpoint: String, body: T) {
        circuitBreaker.apply {
            client.post(endpoint) {
                this.body = json.write(body)
            }
        }
    }

    companion object {
        private val json = defaultSerializer()
        private val client = HttpClient(OkHttp) {
            install(Logging) {
                level = INFO
            }

            install(JsonFeature) {
                serializer = KotlinxSerializer(
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
