package com.github.brunomartinscorrea.health

import com.github.brunomartinscorrea.adapter.http.Controller
import com.github.brunomartinscorrea.adapter.http.HttpRequest

class HealthControllerImpl : Controller<HealthDto> {
    override suspend fun get(request: HttpRequest<HealthDto>) = request.response(body = HealthDto(true))
}
