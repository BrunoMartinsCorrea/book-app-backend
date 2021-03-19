package com.github.brunomartinscorrea.config.shared

import com.github.brunomartinscorrea.adapter.http.Controller
import com.github.brunomartinscorrea.adapter.http.HttpRequest
import com.github.brunomartinscorrea.adapter.http.HttpResponse
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType.Companion.parse
import io.ktor.http.HttpStatusCode.Companion.fromValue
import io.ktor.request.httpMethod
import io.ktor.request.receiveOrNull
import io.ktor.request.uri
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.toMap

object HttpClientExtension {
    inline fun <reified T> Route.setControllerRoute(
        controller: Controller<T>,
        resourcePath: String = "/",
        postPath: String = "",
        getPath: String = "",
        putPath: String = "",
        patchPath: String = "",
        deletePath: String = "",
        build: Route.() -> Unit = { }
    ) = route("/$resourcePath") {
        post(postPath) {
            setController(controller::post)
        }

        get(getPath) {
            setController(controller::get)
        }

        put(putPath) {
            setController(controller::put)
        }

        patch(patchPath) {
            setController(controller::patch)
        }

        delete(deletePath) {
            setController(controller::delete)
        }
    }.apply(build)

    suspend inline fun <reified T> PipelineContext<Unit, ApplicationCall>.setController(
        crossinline function: suspend (HttpRequest<T>) -> HttpResponse<T>
    ) {
        val request = HttpRequestImpl<T>(
            method = call.request.httpMethod.value,
            uri = call.request.uri,
            headers = call.request.headers.toMap().map { it.key to it.value.first() }.toMap(),
            pathVariables = call.parameters.entries().map { it.key to it.value.first() }.toMap(),
            body = call.receiveOrNull()
        )
        val response = function(request)

        call.response.status(fromValue(response.statusCode))

        response.headers.forEach {
            call.response.header(it.key, it.value)
        }

        response.body?.let {
            when (it) {
                is String -> call.respondText(text = it, contentType = parse(response.contentType))
                else -> call.respond(it)
            }
        }
    }
}
