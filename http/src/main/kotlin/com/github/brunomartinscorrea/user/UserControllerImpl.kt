package com.github.brunomartinscorrea.user

import com.github.brunomartinscorrea.adapter.http.Controller
import com.github.brunomartinscorrea.adapter.http.HttpRequest
import com.github.brunomartinscorrea.adapter.http.HttpResponse
import com.github.brunomartinscorrea.adapter.service.UserService
import com.github.brunomartinscorrea.exception.database.RegisterNotFound
import com.github.brunomartinscorrea.exception.http.HttpException

class UserControllerImpl(
    private val service: UserService
) : Controller<UserDto> {

    override suspend fun post(request: HttpRequest<UserDto>): HttpResponse<UserDto> {
        val user = request.body!!.toEntity()
        val savedUser = service.save(user)
        val dto = UserDto.toDto(savedUser)

        return request.response(
            status = 201,
            body = dto,
            headers = mapOf("Location" to request.uri.plus("/${dto.id}"))
        )
    }

    override suspend fun get(request: HttpRequest<UserDto>) = try {
        val id = request.pathVariables["id"]!!.toLong()
        val user = service.findById(id)
        val dto = UserDto.toDto(user)

        request.response(
            body = dto
        )
    } catch (ex: RegisterNotFound) {
        throw HttpException(404, ex.message, ex)
    }

    override suspend fun put(request: HttpRequest<UserDto>): HttpResponse<UserDto> {
        return super.put(request)
    }

    override suspend fun delete(request: HttpRequest<UserDto>): HttpResponse<UserDto> {
        val id = request.pathVariables["id"]!!.toLong()
        service.delete(id)
        return request.response()
    }
}
