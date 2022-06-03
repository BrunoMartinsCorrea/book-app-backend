package com.github.brunomartinscorrea.config.server

import com.github.brunomartinscorrea.adapter.http.Controller
import com.github.brunomartinscorrea.adapter.http.HttpServer
import com.github.brunomartinscorrea.config.shared.HttpClientExtension.setControllerRoute
import com.github.brunomartinscorrea.exception.http.HttpException
import com.github.brunomartinscorrea.health.HealthDto
import com.github.brunomartinscorrea.user.UserDto
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.fromValue
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.slf4j.event.Level.INFO

class HttpServerImpl(
    private val port: Int,
    private val contextPath: String,
    private val rootController: Controller<String>,
    private val healthController: Controller<HealthDto>,
    private val userController: Controller<UserDto>
) : HttpServer {
    override fun start() {
        embeddedServer(Netty, port) {
            install(CallLogging) {
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

            install(StatusPages) {
                val defaultMessageError = "Something went wrong!"

                exception<Exception> { call, cause ->
                    call.respondText(
                        status = InternalServerError,
                        text = cause.message ?: defaultMessageError
                    )
                }

                exception<HttpException> { call, cause ->
                    call.respondText(
                        status = fromValue(cause.statusCode),
                        text = cause.message ?: defaultMessageError
                    )
                }
            }

            routing {
                route(contextPath) {
                    setControllerRoute(controller = rootController)
                    setControllerRoute(controller = healthController, resourcePath = "/health")
                    setControllerRoute(controller = userController, resourcePath = "/user", getPath = "{id}")
                }
            }
        }.start(wait = true)
    }
}
