package com.github.brunomartinscorrea.utils

interface EntityConverter<E> {
    fun toEntity(): E
}
