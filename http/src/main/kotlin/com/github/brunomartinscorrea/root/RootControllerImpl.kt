package com.github.brunomartinscorrea.root

import com.github.brunomartinscorrea.adapter.http.Controller
import com.github.brunomartinscorrea.adapter.http.HttpRequest

class RootControllerImpl : Controller<String> {
    override suspend fun get(request: HttpRequest<String>) = request.response(
        body = ClassLoader.getSystemResource("assets/index.html").readText(),
        contentType = "text/html"
    )
}
