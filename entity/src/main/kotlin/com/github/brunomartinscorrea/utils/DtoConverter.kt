package com.github.brunomartinscorrea.utils

interface DtoConverter<D, E> {
    fun toDto(entity: E): D
}
